package aseds_snake;

import java.util.LinkedList;
import java.util.Random;

//classe pour representer le serpent
public class Snake {
	private int length;
	private LinkedList<Point> corp;
	private char direction;

	public Snake() {
		// creation d'un nouveau serpent avec 4 entités et direction par defaut 'droit'
		this.length = 4;
		corp = new LinkedList<Point>();
		Random rand = new Random();
		int x = rand.nextInt(50)+10;
		int y = rand.nextInt(10)+5;
		corp.add( new Point(x, y));
		corp.add(new Point(x+1, y));
		corp.add(new Point(x+2, y));
		corp.add(new Point(x+3, y));

		direction = 'd';
	}

	public int getLength() {
		return length;
	}

	public LinkedList<Point> getCorp() {
		return corp;
	}

	public char getDirection() {
		return direction;
	}

	// mettre a jour la direction
	public void setDirection(char direction) {

		switch (direction) {
		case 'h':
			if (this.direction != 'b')
				this.direction = direction;
			break;
		case 'b':
			if (this.direction != 'h')

				this.direction = direction;
			break;
		case 'g':
			if (this.direction != 'd')

				this.direction = direction;
			break;
		case 'd':
			if (this.direction != 'g')

				this.direction = direction;
			break;
		}
	}

	// mise a jour des cordonnées des points
	public void move() {
		Point head = corp.getLast();
		corp.removeFirst();

		switch (direction) {
		case 'h':
			head = new Point(head.getX(), head.getY() - 1);
			break;
		case 'b':
			head = new Point(head.getX(), head.getY() + 1);
			break;
		case 'g':
			head = new Point(head.getX() - 1, head.getY());
			break;
		case 'd':
			head = new Point(head.getX() + 1, head.getY());
			break;
		}
		;
		//
		corp.addLast(head);
	}

//incrementation de la longeur du serpent
	public void addLength() {
		++length;
		corp.addFirst(new Point(corp.getFirst().getX(), corp.getFirst().getY()));
	}

}
