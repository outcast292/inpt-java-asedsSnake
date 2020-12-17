package aseds_snake;

public class ipv4 {
	private String adresse_ipv4;

	// ===============>> Verify

	public static int verify(String adresse_ipv4) {
		try {

			String[] adr = adresse_ipv4.split("\\.");
			int x = adr.length;
			int[] ip = new int[x];
			if (adr.length != 4)

				return 1;

			for (int i = 0; i < adr.length; i++) {
				ip[i] = Integer.parseInt(adr[i]);
				if ((ip[i] <= 0) || (ip[i] >= 255))

					return 1;

			}
			/*
			 * for(int el:ip) { if((el <= 0) && (el >= 255))
			 * 
			 * return 0;
			 * 
			 * }
			 */

			return 0;
		}

		catch (Exception eip) {
			return 1;
		}
	}

	/**
	 * @param adresse_ipv4
	 */
	public ipv4(String adresse_ipv4) {
		this.adresse_ipv4 = adresse_ipv4;

		if (verify(adresse_ipv4) == 0) {

			System.out.println("Adresse valide");

		}

		else if (verify(adresse_ipv4) == 1)
			System.out.println("Adresse NON valide");

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ipv4 r = new ipv4("255.255.255.289");

	}
}