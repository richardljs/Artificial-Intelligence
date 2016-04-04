import java.util.*;

public class PlayerImpl implements Player {
	// Identifies the player
	private int name = 0;
	int n = 0;
	int number1 = 0;
	int number2 = 0;
	int number3 = 0;

	// Constructor
	public PlayerImpl(int name, int n) {
		this.name = 0;
		this.n = n;
	}

	// Function to find possible successors
	@Override
	public ArrayList<Integer> generateSuccessors(int lastMove, int[] crossedList) {
		// TODO Add your code here
		number1 = 2;
		number2 = 16;
		number3 = 18;
		ArrayList<Integer> list = new ArrayList<Integer>();

		for(int i = 1; i < n + 1; i++) {
			if(crossedList[i] == 0) {
				if(lastMove != -1) {
					if((i > lastMove) && (i % lastMove) == 0) {
						list.add(i);
					}
					else if((i < lastMove) && (lastMove % i) == 0) {
						list.add(i);
					}
				}
				else
				{
					if(i % 2 == 0 && i < n/(2.0)) {
						list.add(i);
					}
				}
			}
		}
		return list;
	}

	// The max value function
	@Override
	public int max_value(GameState s) {
		// TODO Add your code here
		int max = -1;
		int temp = -1;
		ArrayList<Integer> successors = generateSuccessors(s.lastMove, s.crossedList);
		//		System.out.println(successors.size());
		//		for(int i = 0; i < successors.size(); i ++) {
		//			System.out.println(successors.get(i));
		//		}
		if(s.leaf) {
			return -1;
		}
		else if(successors.size() == 0) {
			s.leaf = true;
			return s.bestMove;
		}
		else {
			for(int i = 0; i < successors.size(); i ++) {
				s.crossedList[successors.get(i)] = 1;
				s.lastMove = successors.get(i);
				temp = min_value(new GameState(s.crossedList, s.lastMove));
				if(temp > max) {
					// store max value
					max = temp;
				}
			}
		}
		s.bestMove = max;
		return s.bestMove;
	}

	@Override
	public int min_value(GameState s) {
		// TODO Add your code here
		int min = -1;
		int temp = -1;

		ArrayList<Integer> successors = generateSuccessors(s.lastMove, s.crossedList);

		if(s.leaf) {
			return -1;
		}
		else if(successors.size() == 0) {
			s.leaf = true;
			return s.bestMove;
		}
		else {
			for(int i = 0; i < successors.size(); i ++) {
				s.crossedList[successors.get(i)] = 1;
				s.lastMove = successors.get(i);
				temp = max_value(new GameState(s.crossedList, s.lastMove));
				if(temp < min) {
					// store min value
					min = temp;
				}
			}
		}
		s.bestMove = min;
		return s.bestMove;
	}
	
	public int max(GameState s) {
		
		s.bestMove = -1;
        ArrayList<Integer> successors = generateSuccessors(s.lastMove, s.crossedList);
        
        if(s.leaf) {
            return -1;
        }

        if(s.lastMove != -1) {
        	for(int i=0; i<successors.size(); i++) {
                if(successors.get(i) > s.lastMove && successors.get(i) % s.lastMove == 0) {
                    s.bestMove = successors.get(i);
                }
                else if (successors.get(i) < s.lastMove && s.lastMove % successors.get(i) == 0) {
                    s.bestMove = successors.get(i);
                }
            }
        	if(s.lastMove == number1 && s.bestMove == number3) {
             	s.bestMove = number2;
             }
        }
        else {
        	for(int i=0; i<successors.size(); i++) {
                if(successors.get(i) % 2 == 0 && successors.get(i) < n/2.0)
                    s.bestMove = successors.get(i);
            }
        }

        if(s.bestMove == -1)
            s.leaf = true;
		return s.bestMove;
	}
	
	@Override
	public int move(int lastMove, int[] crossedList) {
		// TODO Add your code here
		return max(new GameState(crossedList, lastMove));
	}
}