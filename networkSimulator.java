
/**
 * William Marks
 * March 8th, 2020
 * The goal of this program is to simulate a WNS server by taking input for the 
 * length and width of the grid and number of nodes, then asking for a packet the user would like to receive
 * and the goal of the server is from the main node it branches off to each individual node within the respective
 * range of 30, and checks if it has the packet, if so, then it removes the packet and sends it back to the main
 * node, if not then it continues searching till it either cannot find the packet, or it runs out of nodes to 
 * check within that range.
 * 
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class networkSimulator {

	static int PACKET_TO_FIND = (int) random(0, 9);

	static int firstName = 1;

	static ArrayList<node> nodeList = new ArrayList<node>();
	static int mValue = 0;
	static node mainNode = new node(mValue, mValue, firstName);

	static int nodeTotal; // Total nodes in the system after deployment, -1 since the first node is the

	static int userPacket;
	int index = firstName;
	static int counterFin;

	static LinkedList<Integer> fillerPacket = new LinkedList<Integer>();

	static ArrayList<Integer> previousPacket = new ArrayList<Integer>();

	/**
	 * Made as a substitute to the main Math.Random() function
	 * 
	 * @param min Double
	 * @param max Double
	 * @return the new randomized value between min and max
	 */
	public static double random(double min, double max) {
		return (int) ((int) min + Math.random() * (max - min));
	}

	/**
	 * takes the value for n as an input
	 * 
	 * @return n Integer
	 */
	public static void nValue() {
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter the amount of nodes: ");
		if (input.hasNextInt()) {
			nodeTotal = input.nextInt();
			if (nodeTotal <= 1) {
				System.out.println("Error: invalid input for nodes");
				System.exit(0);
			}
			nodeTotal -= 1;
		} else {
			System.out.println("Error: invalid input for nodes");
			System.exit(0);
		}
	}

	/**
	 * takes the value for m as an input from the user
	 * 
	 * @return m Integer
	 */
	public static void mValue() {
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter the network length(will be also used for width): ");
		if (input.hasNextInt()) {
			mValue = input.nextInt();
			if (mValue <= 0) {
				System.out.println("Error: invalid input for length");
				System.exit(0);
			}
		} else {
			System.out.println("Error: invalid input for length");
			System.exit(0);
		}
	}

	/**
	 * Used to determine if the user would like to do R1 or R2
	 * 
	 * @return R1 or R2 String
	 */
	public static String choice() {
		String choice = "R1";
		Scanner input = new Scanner(System.in);
		System.out.print("Would you like to run R1 or R2?: ");
		if (input.hasNextLine()) {
			choice = input.nextLine();
			if (choice.equalsIgnoreCase("R1") || choice.equalsIgnoreCase("R2")) {
			} else {
				System.out.println("Error: invalid input for version");
				System.exit(0);
			}
		} else {
			System.out.println("Error: invalid input for version");
			System.exit(0);
		}
		return choice;
	}

	/**
	 * Used to find the packet the program will be searching for to send to the main
	 * node
	 */
	public static void userPacketToFind() {
		Scanner input = new Scanner(System.in);
		int search = 0;
		System.out.print("Please enter the packet you'd like to find for the main node: ");
		if (input.hasNextInt()) {
			userPacket = input.nextInt();
			if (search < 0 || search > 9) {
				System.out.println("Error: invalid input for packet");
				System.exit(0);
			}
		} else {
			System.out.println("Error: invalid input for packet");
			System.exit(0);
		}
	}

	/**
	 * Used to check if the user wants to end the program
	 * 
	 * @return true or false Boolean
	 */
	public static boolean isFinished() {
		Scanner input = new Scanner(System.in);
		String choice = "";

		System.out.print("Would you like to continue running the application?: ");
		if (input.hasNextLine()) {
			choice = input.nextLine();
			if (choice.equalsIgnoreCase("no") || choice.equalsIgnoreCase("yes")) {
			} else {
				System.out.println("Error: invalid choice for continuation.");
				System.exit(0);
			}
		} else {
			System.out.println("Error: invalid choice for continuation.");
			System.exit(0);
		}

		if (choice.equalsIgnoreCase("yes")) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		// initial start functions for taking in inputs
		nValue();
		mValue();
		String decider = choice();
		boolean done = false;

		// begins deploying the nodes around the map.
		mainNode.netDeployment();

		for (int j = 0; j < nodeList.size(); j++) {

			// fills and finds neighbours of the every node
			nodeList.get(j).findNeighbours(nodeList, mainNode);
			nodeList.get(j).packetFill();

		}

		System.out.println();

		do {
			// empties the node list to ensure it can restart the search
			node.empty();

			// to make sure that it searches out past the main node
			mainNode.findNeighbours(nodeList);

			// ensures that there is no null in the calculations
			for (int i = 0; i < nodeList.size(); i++) {
				if (nodeList.get(i).closestNeighbour_first == null) {
					node.changeFirst(i);
				}
				if (nodeList.get(i).closestNeighbour_second == null) {
					node.changeSecond(i);
				}
			}

			int num = 0;

			/*
			 * The goal of this switch case is to differentiate between the two different
			 * versions of the program that can be run.
			 */
			switch (decider) {

			// Case "R1" finds a random packet between 0-9 to find and send to the main node
			case "R1":
				PACKET_TO_FIND = (int) random(0, 9);
				num = mainNode.find(PACKET_TO_FIND);
				break;

			// Case "R2" prompts the user to enter the packet they'd like to find and send
			// to the main node
			case "R2":
				userPacketToFind();
				System.out.println();
				num = mainNode.userFind(userPacket);
				break;

			// this default is to 100% ensure that if any error somehow gets passed the
			// choice section it will terminate the program.
			default:
				System.out.println("Error: something went wrong somewhere...");
				System.exit(0);
			}

			// resets the exit counter for the find function in node()
			counterFin = 0;

			// if the packet was not found it displays an appropriate error message.
			if (num < 0) {
				// was not found

				System.out.println("Packet is not found");
			}

			// checks if the program is ready to exit
			if (mainNode.packets.size() < 10 && decider.equalsIgnoreCase("R2")) {
				System.out.println();
				done = isFinished();
			}

			// empties the node list to ensure it can restart the search
			node.reset();

			// checks if the main node size is equal to 10
			if (mainNode.packets.size() >= 10) {
				System.out.println("-----------------------------------");
				System.out.println();
				System.out.println("The main node has be filled with 10 packets.");
				System.out.println("Main node packets: " + mainNode.packets);
				System.exit(0);
			}
			System.out.println();
			System.out.println("-----------------------------------");
			System.out.println();

		} while (done != true || mainNode.packets.size() == 10);

	}

}

/**
 * 
 * March 8, 2020 
 * The goal of this class is to handle all the node side of
 * things, including deploying them, finding the closest neighbors and sending
 * the packets across them.
 */
class node extends networkSimulator {

	// initializing all the node variables
	double x;
	double y;
	double range = 30;

	int packMax = 9;
	int totalPacks = 5;
	int name = 0;
	int packetLocation;
	int c;

	boolean askedF = false;
	boolean askedS = false;
	boolean checked = false;
	boolean check;

	node closestNeighbour_first;
	node closestNeighbour_second;

	LinkedList<Integer> packets = new LinkedList<Integer>();

	/**
	 * Constructor
	 * 
	 * @param x    Double
	 * @param y    Double
	 * @param name Integer 
	 *             Ensures each node is placed appropriately in within the
	 *             confines of the length and width as well as assigns a appropriate
	 *             name between 2-infinity, since 1 is reserved for the main node.
	 */
	node(double x, double y, int name) {
		this.name = name;

		this.x = random(0, mValue);
		this.y = random(0, mValue);

		while (!inRange(x, y)) {
			this.x = random(0, mValue);
			this.y = random(0, mValue);
		}
	}

	/**
	 * Adds a new node onto the map and increments their name.
	 */
	public void netDeployment() {
		for (int i = 0; i < nodeTotal; i++) {
			index++;
			nodeList.add(new node(this.x, this.y, index));
		}
	}

	/**
	 * Determines if the new node is within the range of the length and width
	 * 
	 * @param x Double
	 * @param y Double
	 * @return True or False depending on if it is within the boundary
	 */
	public boolean inRange(double x, double y) {
		if (Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) < (range * range)) {
			return true;
		}
		return false;
	}

	/**
	 * Used for checking the range
	 * 
	 * @param x Double
	 * @param y Double
	 * @return the range calculation
	 */
	public double printRange(double x, double y) {
		return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2);
	}

	/**
	 * Used for checking the range
	 * 
	 * @param x  Double
	 * @param y  Double
	 * @param x2 Double
	 * @param y2 Double
	 * @return the range calculation
	 */
	public double printRange(double x, double y, double x2, double y2) {
		return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2);
	}

	/**
	 * Used to find first and second closest neighbour of each node
	 * 
	 * @param testing ArrayList<node>
	 * @param main    node
	 */
	public void findNeighbours(ArrayList<node> testing, node main) {

		double closestRange = printRange(main.x, main.y);
		double secondClosest = printRange(main.x, main.y);
		int indexF = -1;
		int indexS = -1;

		this.askedF = true;
		this.askedS = true;

		// This for loop finds the first closest neighbor
		for (int i = 0; i < nodeTotal; i++) {
			if (closestRange > printRange(testing.get(i).x, testing.get(i).y) && testing.get(i).askedF == false) {
				closestRange = printRange(testing.get(i).x, testing.get(i).y);
				indexF = i;
			}
		}

		// This for loop finds the second closest neighbor
		for (int i = 0; i < nodeTotal; i++) {
			if (secondClosest > printRange(testing.get(i).x, testing.get(i).y) && testing.get(i).askedS == false
					&& i != indexF) {
				secondClosest = printRange(testing.get(i).x, testing.get(i).y);
				indexS = i;

			}
			testing.get(i).askedS = true;
		}

		// Resets their asked boolean
		for (int i = 0; i < nodeTotal; i++) {
			testing.get(i).askedF = false;
			testing.get(i).askedS = false;
		}

		if (indexF != -1) {
			closestNeighbour_first = testing.get(indexF);
		}

		if (indexS > -1) {
			closestNeighbour_second = testing.get(indexS);
		}
	}

	/**
	 * used to find the first node from the main node.
	 * 
	 * @param testing ArrayList<node>
	 */
	void findNeighbours(ArrayList<node> testing) {
		double closestRange = printRange(testing.get(0).x, testing.get(0).y);
		int index = 0;

		this.askedF = true;

		// This for loop finds the first closest neighbor
		for (int i = 0; i < nodeTotal; i++) {

			if (closestRange > printRange(testing.get(i).x, testing.get(i).y) && testing.get(i).askedF == false) {
				closestRange = printRange(testing.get(i).x, testing.get(i).y);
				index = i;
			}
			testing.get(i).askedF = true;
		}

		// Resets their asked state
		for (int i = 0; i < nodeTotal; i++) {
			testing.get(i).askedF = false;
		}

		if (index > -1) {
			closestNeighbour_first = testing.get(index);
			closestNeighbour_second = testing.get(index);
		}
	}

	/**
	 * Fills each node with 5 numbers between 0-9
	 */
	public void packetFill() {
		for (int i = 0; i < totalPacks; i++) {
			packets.add((int) random(1, packMax));
		}
		// System.out.println("\n");
	}

	/**
	 * Searches the node to see if the packet is inside of it.
	 * 
	 * @param packet Integer
	 * @return Either the packed was found or -1 if the packet was not found.
	 * @throws Exception
	 */
	public int find(int packet) throws Exception {
		boolean contains = false;
		int packetInterest = -1;
		node previous = null;
		boolean found = false;
		// checks if it's scanned through 100 packets
		// if so declares the run as not found
		if (counterFin != 100) {
			try {
				try {
					if (nodeList.get(this.name).check == false) {
						System.out.println("Searching for packet " + packet + " in node " + this.name);
					}
				} catch (Exception e) {
					System.out.println("Searching for packet " + packet + " in node " + this.name);
				}

				// scans through the packets inside of a node and checks if they have the packet
				// being looked for
				for (int i = 0; i < packets.size(); i++) {
					counterFin++;
					if (packets.get(i) == packet) {
						contains = true;
						System.out.println("Packet found!");
						System.out.println();
						packTransmission(packet, i);
						return packets.get(i);
					}
				}

				// if counterFin gets incremented to 2, resets it back to 0
				if (counterFin == 2) {
					counterFin = 0;
				}

				if (nodeList.get(this.name).check == true || closestNeighbour_first == previous
						|| closestNeighbour_second != previous) {
					previous = closestNeighbour_second;
					nodeList.get(this.name).check = true;
					packetInterest = closestNeighbour_second.find(packet);
				}

				// checks the first neighbour
				if (contains == false && (closestNeighbour_first != null || closestNeighbour_first != previous)
						&& nodeList.get(this.name).check == false) {
					previous = closestNeighbour_first;
					nodeList.get(this.name).check = true;
					found = true;
					packetInterest = closestNeighbour_first.find(packet);
				}

				// if it hasn't been found, it checks the second neighbour
				if (found == false) {
					if (contains == false && (closestNeighbour_second != null || closestNeighbour_second != previous)
							&& nodeList.get(this.name).check == false) {
						previous = closestNeighbour_second;
						nodeList.get(this.name).check = true;
						packetInterest = closestNeighbour_second.find(packet);
					}

				}

				return packetInterest;
			} catch (Exception e) {
				return packetInterest;
			}
		}
		return -1;
	}

	/**
	 * Searches the node to see if the packet is inside of it.
	 * 
	 * @param packet Integer
	 * @return Either the packed was found or -1 if the packet was not found.
	 * @throws Exception
	 */
	public int userFind(int packet) throws Exception {
		boolean contains = false;
		int packetInterest = -1;
		node previous = null;
		boolean found = false;
		// checks if it's scanned through 100 packets
		// if so declares the run as not found
		if (counterFin != 100) {
			try {
				try {
					if (nodeList.get(this.name).check == false) {
						System.out.println("Searching for packet " + packet + " in node " + this.name);
					}
				} catch (Exception e) {
					System.out.println("Searching for packet " + packet + " in node " + this.name);
				}
				// scans through the packets inside of a node and checks if they have the packet
				// being looked for
				for (int i = 0; i < packets.size(); i++) {
					counterFin++;
					if (packets.get(i) == packet) {
						contains = true;
						System.out.println("Packet found!");
						System.out.println();
						packTransmission_user(packet, i);
						return packets.get(i);
					}
				}
				// if counterFin gets incremented to 2, resets it back to 0
				if (counterFin == 2) {
					counterFin = 0;
				}

				if (nodeList.get(this.name).check == true || closestNeighbour_first == previous
						|| closestNeighbour_second != previous) {
					previous = closestNeighbour_second;
					nodeList.get(this.name).check = true;
					packetInterest = closestNeighbour_second.userFind(packet);
				}
				// checks the first neighbour
				if (contains == false && (closestNeighbour_first != null || closestNeighbour_first != previous)
						&& nodeList.get(this.name).check == false) {
					previous = closestNeighbour_first;
					nodeList.get(this.name).check = true;
					found = true;
					packetInterest = closestNeighbour_first.userFind(packet);
				}
				// if it hasn't been found, it checks the second neighbour
				if (found == false) {
					if (contains == false && (closestNeighbour_second != null || closestNeighbour_second != previous)
							&& nodeList.get(this.name).check == false) {
						previous = closestNeighbour_second;
						nodeList.get(this.name).check = true;
						packetInterest = closestNeighbour_second.userFind(packet);
					}

				}
				return packetInterest;
			} catch (Exception e) {
				return packetInterest;
			}
		}
		return -1;
	}

	/**
	 * This is the deviation of packTransmission() used for the user input version
	 * 
	 * @param packet Integer
	 * @param i      Integer
	 * @throws Exception
	 */
	public void packTransmission_user(int packet, int i) throws Exception {
		addBack();
		int c = 2;
		if (fillerPacket.size() == 1) {
			mainNode.packets.add(fillerPacket.get(0));
		}
		try {
			nodeList.get(this.name - 2);
		} catch (Exception e) {
			c = -1;
		}
		System.out.println("Sender: Node " + Integer.toString(this.name) + " with packets: "
				+ nodeList.get(this.name - c).packets);
		System.out.println("Reciver: Main node with packets: " + mainNode.packets);
		System.out.println();
		System.out.println(
				"Sending packet " + userPacket + " from Node " + Integer.toString(this.name) + " to Main Node");
		System.out.println();
		nodeList.get(this.name - c).packets.remove(i);
		System.out.println(
				"Node " + Integer.toString(this.name) + " updated packets: " + nodeList.get(this.name - c).packets);
		nodeList.get(this.name - c).packets.addLast(0);
		mainNode.packets.add(packet);
		System.out.println("Main node updated packets: " + mainNode.packets);
		System.out.println();
		counterFin = 0;
	}

	/**
	 * Takes the number and removes it from the specific node and adds it to the
	 * main node
	 * 
	 * @param packet Integer
	 * @param i      Integer
	 * @throws Exception
	 */
	public void packTransmission(int packet, int i) throws Exception {
		addBack();
		int c = 2;
		if (fillerPacket.size() == 1) {
			mainNode.packets.add(fillerPacket.get(0));
		}
		try {
			nodeList.get(this.name - 2);
		} catch (Exception e) {
			c = -1;
		}
		System.out.println("Sender: Node " + Integer.toString(this.name) + " with packets: "
				+ nodeList.get(this.name - c).packets);
		System.out.println("Reciver: Main node with packets: " + mainNode.packets);
		System.out.println();
		System.out.println(
				"Sending packet " + PACKET_TO_FIND + " from Node " + Integer.toString(this.name) + " to Main Node");
		System.out.println();
		nodeList.get(this.name - c).packets.remove(i);
		System.out.println(
				"Node " + Integer.toString(this.name) + " updated packets: " + nodeList.get(this.name - c).packets);
		nodeList.get(this.name - c).packets.addLast(0);
		mainNode.packets.add(packet);
		System.out.println("Main node updated packets: " + mainNode.packets);
		System.out.println();
		counterFin = 0;
	}

	/**
	 * removes the closest in the first if it is null and replaces it with an
	 * appropriate counter part
	 * 
	 * @param i
	 */
	public static void changeFirst(int i) {
		try {
			nodeList.get(i).closestNeighbour_first = nodeList.get(i - 1).closestNeighbour_first;
		} catch (Exception e) {
			nodeList.get(i).closestNeighbour_first = nodeList.get(i + 1).closestNeighbour_first;
		}
	}

	/**
	 * removes the closest in second if it is null and replaces it with an
	 * appropriate counter part
	 * 
	 * @param i
	 */
	public static void changeSecond(int i) {
		try {
			nodeList.get(i).closestNeighbour_second = nodeList.get(i - 1).closestNeighbour_second;
		} catch (Exception e) {
			nodeList.get(i).closestNeighbour_second = nodeList.get(i + 1).closestNeighbour_second;
		}
	}

	/**
	 * resets the nodeList back to false to ensure that it can scan its neighbors
	 * for a new packet again
	 */
	public static void reset() {
		for (int i = 0; i < nodeList.size(); i++) {
			nodeList.get(i).check = false;
		}
	}

	/**
	 * empties the list to ensure duplicate packets can be put into the mainNode.
	 */
	public static void empty() {
		if (!(mainNode.packets.size() == 0)) {
			for (int i = 0; i < mainNode.packets.size(); i++) {
				fillerPacket.add(mainNode.packets.get(i));
				mainNode.packets.remove(i);
			}
		}
		for (int i = 0; i < mainNode.packets.size(); i++) {
			fillerPacket.add(mainNode.packets.get(i));
			mainNode.packets.remove(i);
		}

		if (mainNode.packets.size() == 1) {
			fillerPacket.add(mainNode.packets.get(0));
			mainNode.packets.remove(0);
		}

	}

	/**
	 * adds back removed packets from the mainNode
	 */
	public static void addBack() {
		for (int i = 0; i < fillerPacket.size(); i++) {
			mainNode.packets.add(fillerPacket.get(i));
			fillerPacket.remove(i);
		}

		for (int i = 0; i < fillerPacket.size(); i++) {
			mainNode.packets.add(fillerPacket.get(i));
			fillerPacket.remove(i);
		}

		if ((fillerPacket.size() == 2)) {
			mainNode.packets.add(fillerPacket.get(1));
			fillerPacket.remove(1);

			if ((fillerPacket.size() == 1)) {
				mainNode.packets.add(fillerPacket.get(0));
				fillerPacket.remove(0);

			}

		}
	}
}