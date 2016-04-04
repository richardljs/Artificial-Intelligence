import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Iterator;


public class AStarSearchImpl implements AStarSearch {
	public static Comparator<State> comparator = new Comparator<State>() {
		@Override
		public int compare(State s1, State s2) {

			if (s1.realCost + s1.heuristicCost - s2.realCost - s2.heuristicCost == 0)
				return s1.opSequence.compareTo(s2.opSequence);
			else 
				return (s1.realCost + s1.heuristicCost - s2.realCost - s2.heuristicCost);
		}
	};
	/**
	 * Given the initial configuration, return the search result
	 * @param initConfig: the initial configuration of the board
	 * @param modeFlag: specify which heuristic your should use
	 */
	@Override	
	public SearchResult search(String initConfig, int modeFlag) {
		// TODO Add your code here
		String opSequence = "";
		int realCost = 0;
		int heuristicCost = 0;
		String startString = initConfig;
		int numPoppedStates = -1;
		int count = 0;
		HashMap <String, State> map = new HashMap<String, State>();
		HashMap <String, State> mapClose = new HashMap<String, State>();
		HashMap <String, State> mapOpen = new HashMap<String, State>();

		PriorityQueue<State> OPEN = new PriorityQueue<State>(100000, comparator);
		PriorityQueue<State> CLOSED = new PriorityQueue<State>(100000, comparator);
		
		String tempSeq = "";

		State state = new State(startString, realCost, heuristicCost, opSequence);
		state.heuristicCost = getHeuristicCost(startString, modeFlag);

		OPEN.offer(state);

		// while OPEN is not empty do
		while(!OPEN.isEmpty()) {
			// Remove from OPEN and place in CLOSED a state n for which f(n) is the minimum
			// n should have the lowest fscore or alphabetically smallest among lowest fscore
			State n = OPEN.poll();
			
			//System.out.println(n.config);
			numPoppedStates ++;
			count ++;
//			tempSeq += n.opSequence;
			CLOSED.add(n);
			mapClose.put(n.config, n);
//			System.out.println(n.realCost);
//			System.out.println(mapClose.get(n.opSequence));
			
//			SearchResult res = new SearchResult(n.config, null, 0);
//			System.out.println("pick " + n.config);
//			res.printConfig();
//			System.out.println("");

			if(checkGoal(n.config)) {
				tempSeq = n.opSequence;
				
				State s = n;
				while(s.getParent() != null) {
					s = s.getParent();
					tempSeq += s.opSequence;
				}
				String result = tempSeq;
				if(tempSeq != "") {
					result = new StringBuffer(tempSeq).reverse().toString().trim();
				}
//				for(int i = parents.size() - 1; i >= 0; i--) {
//					System.out.print(parents.get(i).config);
//				}
//				System.out.println("12345");
//				System.out.println(n.config);
				return new SearchResult(n.config, result, numPoppedStates);
			}
 
			ArrayList<State> successors = new ArrayList<State>();
			State stateA = new State(move(n.config, 'A'), 0, 0, "");
						stateA.heuristicCost = getHeuristicCost(stateA.config, modeFlag);
			//			System.out.println(stateA.heuristicCost);
						stateA.realCost = count;
			stateA.config = move(n.config, 'A');
			stateA.opSequence += 'A';
			successors.add(stateA);

			State stateB = new State(move(n.config, 'B'), 0, 0, "");
						stateB.heuristicCost = getHeuristicCost(stateB.config, modeFlag);
			//			System.out.println(stateB.heuristicCost);
						stateB.realCost = count;
			stateB.config = move(n.config, 'B');
			stateB.opSequence += 'B';
			successors.add(stateB);

			State stateC = new State(move(n.config, 'C'), 0, 0, "");
						stateC.heuristicCost = getHeuristicCost(stateC.config, modeFlag);
			//			System.out.println(stateC.heuristicCost);
						stateC.realCost = count;
			stateC.config = move(n.config, 'C');
			stateC.opSequence += 'C';
			successors.add(stateC);

			State stateD = new State(move(n.config, 'D'), 0, 0, "");
						stateD.heuristicCost = getHeuristicCost(stateD.config, modeFlag);
			//			System.out.println(stateD.heuristicCost);
						stateD.realCost = count;
			stateD.config = move(n.config, 'D');
			stateD.opSequence += 'D';
			successors.add(stateD);

			State stateE = new State(move(n.config, 'E'), 0, 0, "");
						stateE.heuristicCost = getHeuristicCost(stateE.config, modeFlag);
			//			System.out.println(stateE.heuristicCost);
						stateE.realCost = count;
			stateE.config = move(n.config, 'E');
			stateE.opSequence += 'E';
			successors.add(stateE);

			State stateF = new State(move(n.config, 'F'), 0, 0, "");
						stateF.heuristicCost = getHeuristicCost(stateF.config, modeFlag);
			//			System.out.println(stateF.heuristicCost);
						stateF.realCost = count;
			stateF.config = move(n.config, 'F');
			stateF.opSequence += 'F';
			successors.add(stateF);

			State stateG = new State(move(n.config, 'G'), 0, 0, "");
						stateG.heuristicCost = getHeuristicCost(stateG.config, modeFlag);
			//			System.out.println(stateG.heuristicCost);
						stateG.realCost = count;
			stateG.config = move(n.config, 'G');
			stateG.opSequence += 'G';
			successors.add(stateG);
			
			State stateH = new State(move(n.config, 'H'), 0, 0, "");
						stateH.heuristicCost = getHeuristicCost(stateH.config, modeFlag);
			//			System.out.println(stateH.heuristicCost);
						stateH.realCost = count;
			stateH.config = move(n.config, 'H');
			stateH.opSequence += 'H';
			successors.add(stateH);


			for(int i = 0; i < successors.size(); i ++) {
				State n1 = successors.get(i);
				//				int g = n1.opSequence.length();
				//				int h = getHeuristicCost(n1.config, modeFlag);
				//				int f = g + h;
				int hn1 = 0;

				if(!(map.containsKey(n1.config))) {
					//TODO
					//Set h(n'), g(n'), and f(n')
					//Place n' on OPEN
					n1.setParent(n);
					hn1 = getHeuristicCost(n1.config, modeFlag);
					n1.heuristicCost = hn1;
					OPEN.offer(n1);
					mapOpen.put(n1.config, n1);

					map.put(n1.config, n1);
//										System.out.println(n1.realCost);
//										System.out.println(" " + map.get(n1.opSequence));
//										System.out.println(map.size());
					
				}
				else {
					if(mapClose.containsKey(n1.config)) {
//						System.out.println(" " + n1.opSequence);
//						System.out.println(" " + n1.realCost);
						if(mapClose.get(n1.config).realCost > n1.realCost) {
//							System.out.println("hi");
//							System.out.println( "  " + CLOSED.size());
							CLOSED.remove(mapClose.get(n1.config));
//							System.out.println( "  " + CLOSED.size());
//							System.out.println(" " +mapClose.size());
							mapClose.remove(n1.config);
//							System.out.println(" " +mapClose.size());
							OPEN.offer(n1);
							mapOpen.put(n1.config, n1);
//							SearchResult res2 = new SearchResult(n1.config, null, 0);
//							res2.printConfig();
//							System.out.println( "A. " + n1.opSequence);
//							System.out.println( "B. " + n1.realCost);
//							System.out.println( "C. " + n1.heuristicCost);
							n1.setParent(n);
						}
					}
					else {
//						System.out.println("123");
//						System.out.println(n1.opSequence);
//						System.out.println(mapOpen.);
//						System.out.println(mapOpen.get(n1.opSequence).realCost);
//						System.out.println("234");
						if(mapOpen.get(n1.config).realCost > n1.realCost) {
//							System.out.println("bye");
//							System.out.println( "  " + OPEN.size());
							OPEN.remove(mapOpen.get(n1.config));
//							System.out.println( "  " + OPEN.size());
//							System.out.println(" " +mapOpen.size());
							mapOpen.remove(n1.config);
//							System.out.println(" " +mapOpen.size());
							OPEN.offer(n1);
							mapOpen.put(n1.config, n1);
//							System.out.println( "A. " + n1.opSequence);
//							System.out.println( "B. " + n1.realCost);
//							System.out.println( "C. " + n1.heuristicCost);
							n1.setParent(n);
						}
					}
				}
				
			}
			
			
//			System.out.println(OPEN.size());
//			Iterator <State> itr = OPEN.iterator();
//			while(itr.hasNext()) {
//				State s = itr.next();
//				System.out.println(s.config +"  " + s.heuristicCost + "   " + s.realCost);
//			}
			



		}

		// Returns null if fails to find a solution
		return null;
	}

	/**
	 * check the configuration is a goal 
	 */
	@Override
	public boolean checkGoal(String config) {
		// TODO Add your code here
		if(config.charAt(6) == config.charAt(7) && config.charAt(6) == config.charAt(8)
				&& config.charAt(6) == config.charAt(11) && config.charAt(6) == config.charAt(12)
				&& config.charAt(6) == config.charAt(15) && config.charAt(6) == config.charAt(16)
				&& config.charAt(6) == config.charAt(17)) {
			return true;
		}

		else {
			return false;
		}
	}

	/**
	 * move the current configuration with specific operation
	 */
	@Override
	public String move(String config, char op) {
		// TODO Add your code here
		String temp;
		String boardOrder = "";
		String[] configuration = config.split("");
		if(op == 'A') {
			temp = configuration[1];
			configuration[1] = configuration[3];
			configuration[3] = configuration[7];
			configuration[7] = configuration[12];
			configuration[12] = configuration[16];
			configuration[16] = configuration[21];
			configuration[21] = configuration[23];
			configuration[23] = temp;
		}

		else if(op == 'B') {
			temp = configuration[2];
			configuration[2] = configuration[4];
			configuration[4] = configuration[9];
			configuration[9] = configuration[13];
			configuration[13] = configuration[18];
			configuration[18] = configuration[22];
			configuration[22] = configuration[24];
			configuration[24] = temp;
		}

		else if(op == 'C') {
			temp = configuration[11];
			configuration[11] = configuration[10];
			configuration[10] = configuration[9];
			configuration[9] = configuration[8];
			configuration[8] = configuration[7];
			configuration[7] = configuration[6];
			configuration[6] = configuration[5];
			configuration[5] = temp;
		}

		else if(op == 'D') {
			temp = configuration[20];
			configuration[20] = configuration[19];
			configuration[19] = configuration[18];
			configuration[18] = configuration[17];
			configuration[17] = configuration[16];
			configuration[16] = configuration[15];
			configuration[15] = configuration[14];
			configuration[14] = temp;
		}

		else if(op == 'E') {
			temp = configuration[24];
			configuration[24] = configuration[22];
			configuration[22] = configuration[18];
			configuration[18] = configuration[13];
			configuration[13] = configuration[9];
			configuration[9] = configuration[4];
			configuration[4] = configuration[2];
			configuration[2] = temp;
		}

		else if(op == 'F') {
			temp = configuration[23];
			configuration[23] = configuration[21];
			configuration[21] = configuration[16];
			configuration[16] = configuration[12];
			configuration[12] = configuration[7];
			configuration[7] = configuration[3];
			configuration[3] = configuration[1];
			configuration[1] = temp;
		}

		else if(op == 'G') {
			temp = configuration[14];
			configuration[14] = configuration[15];
			configuration[15] = configuration[16];
			configuration[16] = configuration[17];
			configuration[17] = configuration[18];
			configuration[18] = configuration[19];
			configuration[19] = configuration[20];
			configuration[20] = temp;
		}

		else if(op == 'H') {
			temp = configuration[5];
			configuration[5] = configuration[6];
			configuration[6] = configuration[7];
			configuration[7] = configuration[8];
			configuration[8] = configuration[9];
			configuration[9] = configuration[10];
			configuration[10] = configuration[11];
			configuration[11] = temp;
		}

		for(int i = 0; i < configuration.length; i ++) {
			boardOrder += configuration[i];
		}
		return boardOrder;
	}

	/**
	 * compute the heuristic cost for current configuration
	 * @param modeFlag TODO
	 */
	@Override
	public int getHeuristicCost(String config, int modeFlag) {		
		// TODO Add your code here
		int block1 = 0;
		int block2 = 0;
		int block3 = 0;
		int heuristicCost = 0;

		// HeuristicCost = 8 - max(n1,n2,n3)
		if(modeFlag == 1) {
			//TODO
			if(config.charAt(6) == '1') {
				block1 ++;
			}
			else if(config.charAt(6) == '2') {
				block2 ++;
			}
			else if(config.charAt(6) == '3') {
				block3 ++;
			}

			if(config.charAt(7) == '1') {
				block1 ++;
			}
			else if(config.charAt(7) == '2') {
				block2 ++;
			}
			else if(config.charAt(7) == '3') {
				block3 ++;
			}

			if(config.charAt(8) == '1') {
				block1 ++;
			}
			else if(config.charAt(8) == '2') {
				block2 ++;
			}
			else if(config.charAt(8) == '3') {
				block3 ++;
			}

			if(config.charAt(11) == '1') {
				block1 ++;
			}
			else if(config.charAt(11) == '2') {
				block2 ++;
			}
			else if(config.charAt(11) == '3') {
				block3 ++;
			}

			if(config.charAt(12) == '1') {
				block1 ++;
			}
			else if(config.charAt(12) == '2') {
				block2 ++;
			}
			else if(config.charAt(12) == '3') {
				block3 ++;
			}

			if(config.charAt(15) == '1') {
				block1 ++;
			}
			else if(config.charAt(15) == '2') {
				block2 ++;
			}
			else if(config.charAt(15) == '3') {
				block3 ++;
			}

			if(config.charAt(16) == '1') {
				block1 ++;
			}
			else if(config.charAt(16) == '2') {
				block2 ++;
			}
			else if(config.charAt(16) == '3') {
				block3 ++;
			}

			if(config.charAt(17) == '1') {
				block1 ++;
			}
			else if(config.charAt(17) == '2') {
				block2 ++;
			}
			else if(config.charAt(17) == '3') {
				block3 ++;
			}

			if(block1 >= block2 && block1 >= block3) {
				heuristicCost = 8 - block1;
			}
			else if(block2 >= block1 && block2 >= block3) {
				heuristicCost = 8 - block2;
			}
			else {
				heuristicCost = 8 - block3;
			}

			return heuristicCost;
		}
		else if(modeFlag == 2) {
			return heuristicCost;
		}
		else if(modeFlag == 3) {
			//TODO
			if(config.charAt(6) == '1') {
				block1 ++;
			}
			else if(config.charAt(6) == '2') {
				block2 ++;
			}
			else if(config.charAt(6) == '3') {
				block3 ++;
			}

			if(config.charAt(7) == '1') {
				block1 ++;
			}
			else if(config.charAt(7) == '2') {
				block2 ++;
			}
			else if(config.charAt(7) == '3') {
				block3 ++;
			}

			if(config.charAt(8) == '1') {
				block1 ++;
			}
			else if(config.charAt(8) == '2') {
				block2 ++;
			}
			else if(config.charAt(8) == '3') {
				block3 ++;
			}

			if(config.charAt(11) == '1') {
				block1 ++;
			}
			else if(config.charAt(11) == '2') {
				block2 ++;
			}
			else if(config.charAt(11) == '3') {
				block3 ++;
			}

			if(config.charAt(12) == '1') {
				block1 ++;
			}
			else if(config.charAt(12) == '2') {
				block2 ++;
			}
			else if(config.charAt(12) == '3') {
				block3 ++;
			}

			if(config.charAt(15) == '1') {
				block1 ++;
			}
			else if(config.charAt(15) == '2') {
				block2 ++;
			}
			else if(config.charAt(15) == '3') {
				block3 ++;
			}

			if(config.charAt(16) == '1') {
				block1 ++;
			}
			else if(config.charAt(16) == '2') {
				block2 ++;
			}
			else if(config.charAt(16) == '3') {
				block3 ++;
			}

			if(config.charAt(17) == '1') {
				block1 ++;
			}
			else if(config.charAt(17) == '2') {
				block2 ++;
			}
			else if(config.charAt(17) == '3') {
				block3 ++;
			}

			if(block1 >= block2 && block1 >= block3) {
				heuristicCost = 8 - (int)(block1/2);
			}
			else if(block2 >= block1 && block2 >= block3) {
				heuristicCost = 8 - (int)(block2/2);
			}
			else {
				heuristicCost = 8 - (int)(block3/2);
			}
			return 0;
		}
		return 0;
	}


}
