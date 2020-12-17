package aseds_snake;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ScreenGame {
	private final Random rand = new Random();
	private final SwingTerminalFrame terminal;
	private final Screen screen;
	private final TextGraphics tg;
	private final TerminalSize terminalSize;
	private Player player = new Player();
	private Snake snake;
	private Point food;
	private int score = 0;
	

	// cette classe va s'occuper de la gestion de l'interface utilisateur et
	// l'ensemble des interactions .
	public ScreenGame() throws IOException, InterruptedException, CloneNotSupportedException {
		terminal = new SwingTerminalFrame(TerminalEmulatorAutoCloseTrigger.CloseOnEscape); // Terminal represente la fenetre (Cadre exterrieure)
		terminal.resize(80, 24);
		terminal.setResizable(false);
		terminal.setTitle("ASEDS SNAKE");
		terminal.setLocationRelativeTo(null); //pour centrer la fentre
		terminal.setIconImage(ImageIO.read(new File("snake.jpg"))); //pour l'icon
		terminal.setVisible(true);
		screen = new TerminalScreen(terminal); // screen represente l'interrieur de la fenetre (contenu)
		tg = terminal.newTextGraphics(); // tg represente un objet permettant la gestion de l'ecriture des caracteressur l'ecran
		terminalSize = terminal.getTerminalSize();

		terminal.setCursorVisible(false); // on cache pardefaut le curseur
		tg.setForegroundColor(TextColor.ANSI.GREEN); // la couleur de text est Vert
		renderNameInputMenu(); // on affiche la page d'identification
		Thread.sleep(250); // temps de veille , pour que l'utilsateur stop de cliquer sur ENtrer
		while (true) { // une boucle infinie pour qu'on peut rejouer si on veut
			switch (renderMainMenu()) { // affichage de menu principal
			case 0:
				startGame(); // comencement de jeu
				break;
			case 1:
				renderHighScore(); // affichage des highscores
				break;
			case 2:
				quitGame(); // quitter le jeu

			}
		}

	}

	private void drawSnake() throws IOException { // cette fonction permet d'afficher le serpent sur l'ecran
		tg.setForegroundColor(TextColor.ANSI.DEFAULT);

		for (int i = 0; i < snake.getLength(); i++) {
			Point point = snake.getCorp().get(i);
			if (i != snake.getLength() - 1)
				drawString(point.getX(), point.getY(), "O");
			else {
				tg.setForegroundColor(TextColor.ANSI.MAGENTA); // tete du serpent
				drawString(point.getX(), point.getY(), "@");

			}
		}
	}

	private void startGame() throws IOException, InterruptedException, CloneNotSupportedException { // fonction qui gere
																									// le fonctionnement
																									// de jeu
		score = 0; // remise a zero de score
		terminal.clearScreen(); // ecran renit
		screen.refresh();
		snake = new Snake();

		tg.setForegroundColor(TextColor.ANSI.GREEN); // changement de couleur de TextGraphics
		tg.drawRectangle(new TerminalPosition(0, 0), terminalSize, '#'); // creation de l'arene ( un rectangle qui
																			// entoure l'ecran)
		drawSnake();

		int counter = -1;
		while (true) { // boucle infinie de jeu
			counter++;
			KeyStroke kp = terminal.pollInput();// lecture non bloquante de la lettre introduite
			if (kp != null) {
				if (kp.getKeyType() == KeyType.Character) { // on verifie si il s'agit d'un caractere
					char c = kp.getCharacter();
					if (c == 'h' || c == 'b' || c == 'g' || c == 'd') { // on verifie si il s'agit d'un des caractere de
																		// control
						snake.setDirection(c); // on change la direction de serpent
					}
				}

			}
			if (counter % 90 != 0) // compteur de rafraichissement
				continue; // on continue l
			if (food == null)
				generateFood();

			LinkedList<Point> body = snake.getCorp();
			Point p = body.getFirst();
			Point p1 = body.getLast();
			if (p1.equals(food))
				snakeFruit();
			snake.move();
			drawString(p.getX(), p.getY(), " ");
			screenRefresh();

			drawSnake();
			showScore();
			screenRefresh();
			if (p1.getX() == 0 || p1.getX() == 79 || p1.getY() == 0 || p1.getY() == 23 // on verifie si la tete de
																						// serpent touche un obstacle
					|| body.subList(0, body.size() - 2).contains(body.getLast())) { // ou si il touche son corps
				player.setScore(score); // si oui on mis a jour le score
				if (player.checkIfHighScore() && score > 0)
					Player.addToHighScore(player); // on enregistre le score si il s'agit de highscore
				switch (renderGameOver()) { // on affiche le menu de fin d jeu
				case 0:
					startGame(); // fonction recursive pour recommencer le jeu
					break;
				case 1:
					return; // le retour nous rammene a la premiere boucle infini qui gere le menu principal
				case 2:
					quitGame(); // on quit le jeu directement

				}
			}
			TimeUnit.MILLISECONDS.sleep(80); // un petit moment de sleep pour que le clavier n'a plus de caractere on
												// cache

		}

	}

	private void quitGame() throws IOException { // fonction pour quiter le jeu
		terminal.exitPrivateMode(); // fermeture de la fenetre
		System.exit(0); // exit de programme avec 0 comme code erreur => execution normal
	}

	public void renderNameInputMenu() throws IOException, InterruptedException { // menu d'introduction du nom
		int x = 10, y = 1;
		StringBuilder st = new StringBuilder(); // on a prefere d'utiliser le stringbuilder pour sa modificabilité
		terminal.clearScreen();
		for (String string : AsciiArt.salute) { // on affiche l'art ascii de BONJOUR
			drawString(x, ++y, string, SGR.BOLD, null);
		}
		y += 3;
		x += 5;
		drawString(x, y, "Votre nom : ");
		x += 12;
		terminal.setCursorVisible(true); //
		while (true) { // boucle infinie pour la lecture du nom

			KeyStroke key = screen.pollInput();
			if (key == null)
				continue;
			else if (key.getKeyType().equals(KeyType.Enter) && st.length() != 0) { // si on clique sur entrer et le nom
																					// n'est pas vide
				break;
			} else if (key.getKeyType().equals(KeyType.Backspace)) { // possibilité du revenir en arriere
				if (st.length() == 0)
					continue;
				terminal.setCursorPosition(x + st.length() - 1, y);
				terminal.putCharacter(' ');
				terminal.setCursorPosition(x + st.length() - 1, y);
				st.setLength(st.length() - 1);

				terminal.flush();

			} else if (key.getKeyType().equals(KeyType.Character)) { // ajout de caractere
				st.append(key.getCharacter());
				drawString(x, y, st.toString());
				screenRefresh();
			} else
				continue;
		}
		terminal.setCursorVisible(false);
		player.setName(st.toString()); // mise a jour du nom de jouer
	}

	public int renderMainMenu() throws IOException, InterruptedException { // gestion menu principal
		int x = 5, y = 1;
		String[] options = { "COMMENCER", "HIGHSCORE", "QUITTER" };
		int selectedOption = 0;

		terminal.clearScreen();
		for (String string : AsciiArt.gameName) { // art ascii du titre principal
			drawString(x, ++y, string, SGR.BOLD, TextColor.ANSI.GREEN);
		}
		y += 3;
		x += 10;
		drawString(x, y, "###########################");
		y += 2;
		drawString(x + 6, y, "Bonjour  " + player.getName()); // affichage du
																// nom de jouer
		y += 2;
		x += 12;
		tg.drawRectangle(new TerminalPosition(15, 11), new TerminalSize(50, 11), '#');

		terminal.flush();
		while (true) { // boucle de gestion de menu
			for (int i = 0; i < options.length; i++) {
				if (i == selectedOption)
					drawString(x, y + i, options[i], SGR.BLINK, TextColor.ANSI.GREEN); // affichage d'entree choisie
																						// avec couleur et style diff
				else
					drawString(x, y + i, options[i]);

			}
			KeyStroke kp = terminal.pollInput();// capture non bloquante de clavier

			if (kp != null) {
				if (kp.getKeyType().equals(KeyType.Enter)) {

					return selectedOption; // si entrer on retour l'entier choisie
				}
				switch (kp.getKeyType()) {// manipulation de menu avec les fleches

				case ArrowUp:
					selectedOption = (selectedOption > 0) ? --selectedOption : selectedOption;
					break;
				case ArrowDown:
					selectedOption = (selectedOption < options.length - 1) ? ++selectedOption : selectedOption;
					break;

				}

				screenRefresh();

			}
		}
	}

	public void renderHighScore() throws IOException, InterruptedException { // affichage de menu de highscore
		int x = 2, y = 1;
		terminal.clearScreen();
		screen.refresh();
		drawString(x + 30, y, "HIGHSCORE BOARD", SGR.UNDERLINE, TextColor.ANSI.YELLOW);

		x += 10;

		y += 8;
		x += 12;
		tg.drawRectangle(new TerminalPosition(15, 6), new TerminalSize(50, 11), '#');

		terminal.flush();
		if (Player.highScore.isEmpty()) // on verifie si la liste des highscores est vide
			drawString(x + 2, y, "Pas de HighScore EnregistrÃ©", SGR.UNDERLINE, TextColor.ANSI.CYAN);
		else
			for (int i = 0; i < Player.highScore.size(); i++) { // affichage des highscores
				Player p = Player.highScore.get(i);
				drawString(x, y + i, (i + 1) + "- " + p.getName() + "    " + p.getScore(), SGR.BOLD,
						TextColor.ANSI.RED);
			}

		drawString(15, 20, "Appuey sur entrer pour retourner au Menu Principal", SGR.BLINK, TextColor.ANSI.GREEN);

		while (true) {

			KeyStroke kp = terminal.pollInput();// capture non bloquante

			if (kp != null) {
				if (kp.getKeyType().equals(KeyType.Enter)) {

					return; // si on clique sur entree on revien au menu principal
				}

			}
		}
	}

	public int renderGameOver() throws IOException, InterruptedException { // menu de fin de jeu
		terminal.setCursorVisible(false);
		food = null;
		int x = 5, y = 1;
		String[] options = { "Recommencer", "Menu Principal", "QUITTER" };
		int selectedOption = 0;

		terminal.clearScreen();
		for (String string : AsciiArt.gameOver) { // asci art de gameover
			drawString(x, ++y, string, SGR.BOLD, TextColor.ANSI.GREEN);
		}
		y += 3;
		x += 10;
		drawString(x, y, "###########################");
		y += 2;
		x += 12;
		tg.drawRectangle(new TerminalPosition(15, 11), new TerminalSize(50, 11), '#');

		terminal.flush();
		while (true) { // gestion menu gameover
			for (int i = 0; i < options.length; i++) { // affichage des elements de menu
				if (i == selectedOption)
					drawString(x, y + i, options[i], SGR.BLINK, TextColor.ANSI.GREEN);
				else
					drawString(x, y + i, options[i]);

			}
			KeyStroke kp = terminal.pollInput();

			if (kp != null) {
				if (kp.getKeyType().equals(KeyType.Enter)) {
					screenClear();
					return selectedOption;

				}
				switch (kp.getKeyType()) {

				case ArrowUp:
					selectedOption = (selectedOption > 0) ? --selectedOption : selectedOption;
					break;
				case ArrowDown:
					selectedOption = (selectedOption < options.length - 1) ? ++selectedOption : selectedOption;
					break;

				}

				screenRefresh();

			}
		}
	}

	// FONCTION POUR ECRIRE UNE LIGNE SUR L'ECRANS avec decoration
	private void drawString(int x, int y, String string, SGR sgr, TextColor textColor) throws IOException {
		tg.setForegroundColor(textColor != null ? textColor : TextColor.ANSI.DEFAULT);// CHANGEMENT DU COULEUR DE TEXTE
		tg.putString(x, y, string, sgr);
		tg.setForegroundColor(TextColor.ANSI.DEFAULT); // REMISE DU COULEUR EN etat INITIAL

		screenRefresh();

	}

	// FONCTION POUR ECRIRE UNE LIGNE SUR L'ECRANS
	private void drawString(int x, int y, String string) throws IOException {
		tg.putString(x, y, string);
		screenRefresh();
	}

	// nettoyage de l'ecran
	private void screenClear() throws IOException {
		screen.clear();
		screenRefresh();
	}

	// raffraichissement de l'ecran
	private void screenRefresh() throws IOException {
		terminal.flush();

	}

	// affichage du score sur l'ecran
	public void showScore() throws IOException {
		drawString(0, terminal.getTerminalSize().getRows() - 1, "SCORE :" + score);
	}

	// serpent mange un fruit
	public void snakeFruit() {
		snake.addLength();
		score++;
		food = null;
	}

	// generation du food
	public void generateFood() throws IOException {
		food = new Point(rand.nextInt(78) + 1, rand.nextInt(22) + 1); // generation d'un point aleatoire
		drawString(food.getX(), food.getY(), "$");
	}

}
