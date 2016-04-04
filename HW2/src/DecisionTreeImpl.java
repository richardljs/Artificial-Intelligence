import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;


/**
 * Fill in the implementation details of the class DecisionTree
 * using this file. Any methods or secondary classes
 * that you want are fine but we will only interact
 * with those methods in the DecisionTree framework.
 * 
 * You must add code for the 5 methods specified below.
 * 
 * See DecisionTree for a description of default methods.
 */
public class DecisionTreeImpl extends DecisionTree {
	private DecTreeNode root;
	private List<String> labels; // ordered list of class labels
	private List<String> attributes; // ordered list of attributes
	private Map<String, List<String>> attributeValues; // map to ordered
														// discrete values taken
														// by attributes
	/**
	 * Answers static questions about decision trees.
	 */
	DecisionTreeImpl() {
		// no code necessary
		// this is void purposefully
	}
	
	/**
	 * Build a decision tree given only a training set.
	 * 
	 * @param train the training set
	 */
	DecisionTreeImpl(DataSet train) {
		
		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;
		List<Instance>instances = train.instances;
		ArrayList<Integer> remainingAtt = new ArrayList<Integer>();
		for (int i = 0; i < train.attributes.size(); i++) {
			remainingAtt.add(i);
		}
		
		buildTree(null, instances, remainingAtt, "");
	}
	

	/**
	 * 	Method used to build a decision tree
	 * 
	 * @param parent, parent node
	 * @param instances, list of instances
	 * @param remainingAtt, the list of all Attributes
	 * @param attributeName, the name of the attribute
	 */
	private void buildTree(DecTreeNode parent, List<Instance> 
	instances, List<Integer> remainingAtt, String attributeName){
		if (remainingAtt.size() == 0){
			parent.addChild(new DecTreeNode(labels.get(getMajority(instances, labels.size())), 
					"", attributeName, true));
			return;
		}
		
		// Storage variables used to store the entropy and info
		Integer highestEntropyId = -1;
		double highestMutualInfo = -1;
		double entropy = calculateEntropy(instances, labels.size());
		
		for (int i = 0; i < remainingAtt.size(); i++) { //For each attribute
			double conditionalEntropy = calcCondEntropy(remainingAtt.get(i), 
					instances, attributeValues.get(attributes.get(remainingAtt.get(i))).size(), 
					labels.size());
			double mutualInformation = entropy - conditionalEntropy;
			if (mutualInformation > highestMutualInfo){
				highestMutualInfo = mutualInformation;
				highestEntropyId = remainingAtt.get(i);
			}
		}
		
		// Create a new node if no parent node
		DecTreeNode currentNode;
		if(parent == null){
			root = new DecTreeNode(labels.get(getMajority(instances, 
					labels.size())), attributes.get(highestEntropyId), 
					"ROOT", isPure(instances) || remainingAtt.size() == 1);
			currentNode = root;
		} else {
			currentNode = new DecTreeNode(labels.get(getMajority(instances, labels.size())), 
					attributes.get(highestEntropyId), attributeName, isPure(instances) || 
					remainingAtt.size() == 0);
			parent.addChild(currentNode);
		}
		
		int numberOfChildren = attributeValues.get(attributes.get(highestEntropyId)).size();
		ArrayList<ArrayList<Instance>> labelInstances = new ArrayList<ArrayList<Instance>>();
		for (int i = 0; i < numberOfChildren; i++) {
			labelInstances.add(new ArrayList<Instance>());
		}
		for (Instance instance : instances) {
			labelInstances.get(instance.attributes.get(highestEntropyId)).add(instance);
		}
		for (int i = 0; i < numberOfChildren; i++) {
			remainingAtt.remove(remainingAtt.indexOf(highestEntropyId));
			buildTree(currentNode, labelInstances.get(i), remainingAtt, attributeValues.
					get(attributes.get(highestEntropyId)).get(i));
			remainingAtt.add(highestEntropyId);
		}
	}

	/**
	 * Build a decision tree given a training set then prune it
	 * using a tuning set.
	 * 
	 * @param train the training set
	 * @param tune the tuning set
	 */
	DecisionTreeImpl(DataSet train, DataSet tune) {
		
		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;
		
		double prevAcc = calculateAccuracy(tune);
		DecTreeNode nodeCandidate = null;
		
		ArrayList<DecTreeNode> nonTerminalNodes = new ArrayList<DecTreeNode>();
		nonTerminalDFS(root, nonTerminalNodes);
		
		double candidateAccuracy = -1;
		int candidateCount = 0;
		
		for (DecTreeNode currNode : nonTerminalNodes) {
			currNode.terminal = true;
			double accuracy = calculateAccuracy(tune);
			currNode.terminal = false;
			int childrenCount = countDFSnodes(currNode);
			if (accuracy > candidateAccuracy || (accuracy == 
					candidateAccuracy && childrenCount > candidateCount)){
				candidateAccuracy = accuracy;
				candidateCount = childrenCount; 
				nodeCandidate = currNode;
			}
		}
		if (candidateAccuracy >= prevAcc) {
			nodeCandidate.children = null;
			nodeCandidate.terminal = true;
		}
	}

	@Override
	public String classify(Instance instance) {
		//Build reverse index
		Map<String, Integer > attributeMap = new HashMap<String, Integer>();
		for (int i = 0; i < attributes.size(); i++) {
			attributeMap.put(attributes.get(i), i);
		}
		DecTreeNode currNode = root;
		while(true){
			if (currNode.terminal){
				break;
			} else {
				int currAttr = attributeMap.get(currNode.attribute);
				int instanceValue = instance.attributes.get(currAttr);
				DecTreeNode foundNode = null;
				for (DecTreeNode node : currNode.children) {
					if (node.parentAttributeValue.equals(attributeValues.
							get(currNode.attribute).get(instanceValue))){
						foundNode = node;
						break;
					}
				}
				currNode = foundNode;
			}
		}
		return currNode.label;
	}

	@Override
	/**
	 * Print the decision tree in the specified format
	 */
	public void print() {
		printTreeNode(root, null, 0);
	}
	
	/**
	 * Prints the subtree of the node
	 * with each line prefixed by 4 * k spaces.
	 */
	public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < k; i++) {
			sb.append("    ");
		}
		String value;
		if(parent == null) {
			value = "ROOT";
		} else {
			value = parent.toString();
		}
		sb.append(value);
		if (p.terminal) {
			sb.append(" (" + p.label + ")");
			System.out.println(sb.toString());
		} else {
			sb.append(" {" + p.attribute + "?}");
			System.out.println(sb.toString());
			for(DecTreeNode child: p.children) {
				child.print(k+1);
			}
		}
	}

	/**
	 * Private function used to calculate the accuracy using the tune set
	 */
	private double calculateAccuracy(DataSet tune){
		int correctClassification = 0;
		for (Instance instance : tune.instances) {
			if(classify(instance).equals(tune.labels.get(instance.label))){
				correctClassification++;
			}
		}
		double accuracy = Double.valueOf(correctClassification)/Double.valueOf
				(tune.instances.size());
		return accuracy;
	}
	
	private void nonTerminalDFS(DecTreeNode node, ArrayList<DecTreeNode> basket){
		if (!node.terminal) {
			basket.add(node);
		} else {
			return;
		}
		for (DecTreeNode currNode : node.children) {
			if (currNode.children != null && currNode.children.size() != 0){
				nonTerminalDFS(currNode, basket);
			}
		}
		
	}
	
	/**
	 * Counts the number of nodes using the Depth First Search
	 */
	private int countDFSnodes(DecTreeNode node){
		int count = 0;
		if (node.terminal == true) {
			return 1;
		}
		for (DecTreeNode currNode : node.children) {
			count += countDFSnodes(currNode);
		}
		return count;
	}
	@Override
	public void rootInfoGain(DataSet train) {
		double entropy = calculateEntropy(train.instances, train.labels.size());
		double mutualInformation = 0.0;
		for (int i = 0; i < train.attributes.size(); i++) { //For each attribute
			double conditionalEntropy = calcCondEntropy(i, train.instances, 
					train.attributeValues.get(train.attributes.get(i)).size(), train.labels.size());
			mutualInformation = entropy - conditionalEntropy;
			System.out.printf("%s %.5f\n", train.attributes.get(i) + " ", mutualInformation);
		}
	}
	
	/**
	 * Calculates the Entropy based on the instances
	 */
	private double calculateEntropy(List<Instance> instances, int noOfLabelTypes){
		int[] labelCounts = new int[noOfLabelTypes];
		int totalInstances = instances.size();
		for (Instance instance : instances) {
			labelCounts[instance.label]++;
		}
		double entropy = 0;
		for (int i = 0; i < labelCounts.length; i++) {
			if (totalInstances != 0 && labelCounts[i] != 0) {
				double probability = Double.valueOf(labelCounts[i])/Double.valueOf(totalInstances); 
				entropy += probability * Math.log10(probability) / Math.log10(2);
			}
		}
		return -entropy;
	}
	
	/**
	 * Finds out the majority vote
	 */
	private int getMajority(List<Instance> instances, int noOfLabelTypes){
		int[] labelCounts = new int[noOfLabelTypes];
		for (Instance instance : instances) {
			labelCounts[instance.label]++;
		}	
		int majorityCandidate = -1;
		int candidateCount = -1;
		for (int i = 0; i < labelCounts.length; i++) {
			if(labelCounts[i] > candidateCount){
				majorityCandidate = i;
				candidateCount = labelCounts[i];
			}
		}
		return majorityCandidate;
	}
	
	private boolean isPure(List<Instance> instances){
		int candidate = -1;
		for (Instance instance : instances) {
			if (candidate == -1){
				candidate = instance.label;
				continue;
			}
			if (candidate != instance.label){
				return false;
			}
		}	
		return true;
	}
	
	/**
	 * Calculates the Conditional Entropy for pruning later on
	 */
	private double calcCondEntropy(int attributeId, List<Instance> 
	instances, int noOfAtrributeTypes, int noOfLabelTypes){

		int[] labelCounts = new int[noOfAtrributeTypes];
		ArrayList<ArrayList<Instance>> labelInstances = new ArrayList<ArrayList<Instance>>();
		//Create empty lists
		for (int i = 0; i < noOfAtrributeTypes; i++) {
			labelInstances.add(new ArrayList<Instance>());
		}
		int totalInstances = instances.size();
		for (Instance instance : instances) {
			labelCounts[instance.attributes.get(attributeId)]++;
			labelInstances.get(instance.attributes.get(attributeId)).add(instance);
		}
		double conditionalEntropy = 0;
		for (int i = 0; i < labelCounts.length; i++) {
			if (totalInstances != 0 && labelCounts[i] != 0) {
				double probability = Double.valueOf(labelCounts[i])/Double.valueOf(totalInstances); 
				double subConditionalEntropy = calculateEntropy(labelInstances.get(i), noOfLabelTypes);
				conditionalEntropy += probability * subConditionalEntropy;
			}
		}
		
		return conditionalEntropy;
	}
}