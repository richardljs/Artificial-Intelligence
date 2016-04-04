import java.util.ArrayList;
/////////////////////////////////////////////////////////////////////
//Title:            KMeans.java
//Semester:         Fall 2014
//
//Author:           Richard Lee - rlee23@wisc.edu
//CS Login:         rlee
//Lecturer's Name:  Jerry Zhu
/////////////////////////////////////////////////////////////////////

/**
 * This class contains a k-means clustering algorithm implementation 
 * method that returns a KMeansResult.
 * 
 * @author Richard Lee
 * 
 */


public class KMeans {
	/**
     * Implementation of the k-means clustering algorithm method.
     * 
     * @param centroids
     * 			2D double array of centroid locations
     * @param instances
     *          2D double array of instance (data points to be clustered)
     *          locations
     * @param threshold
     *          double that is used as a stopping point for difference
     *          successive distortions in order for the program to 
     *          terminate/converge.
     * @return KMeansResult object
     */
	public KMeansResult cluster(double[][] centroids, double[][] instances, double threshold) {	
		// Loop iteration that is used later on for calculating distortions
		int loopItr = -1;
		
		// Initialize variables(integers, arrays, and arraylists) to store values
		int numberOfDimensions = centroids[0].length;
		int[] clusterAssignment = new int[instances.length];
		ArrayList<Double> distortionList = new ArrayList<Double>();
		
		/* storage variables that is used to store cluster ids for cases where there are 2 or
		more same minimum/maximum distances. */
		int store1 = -1;
		int store2 = -1;
		int store3 = -1;
		
		// Loop until program converges based on the threshold distortion
		while(true){
			
			/* Incrementing loop iteration so that distortion can be calculated only after
			  the first loop. */
			loopItr ++;
			
			// Allocate clusters to all instances
			for (int i = 0; i < instances.length; i++) {
				double[] instanceFeatures = instances[i];
				double nearestDistance = -1;
				int nearestCentroid = -1;
				double[] centroidFeatures;
				for (int j = 0; j < centroids.length; j++) {
					centroidFeatures = centroids[j];
					Double distance = calcDistance(centroidFeatures, instanceFeatures);
					/* This checks for same minimum distances and assigns the instance to the
					   one with the smaller cluster id */
					if(distance == nearestDistance){
						// Checking for smaller id, else does nothing
						if(store1 > -1 && clusterAssignment[j] < clusterAssignment[store1]) {
							nearestDistance = distance;
							nearestCentroid = j;
							store1 = j;
						}
					} else if(nearestDistance < 0 || distance < nearestDistance){
						nearestDistance = distance;
						nearestCentroid = j;
						store1 = j;
					}
				}

				clusterAssignment[i] = nearestCentroid;
			}

			/* Assigns children status to all centroids to 0 as its initial
			   meaning there is no instance in them yet. */
			int[] childrenStatus = new int[centroids.length];
			for (int i = 0; i < childrenStatus.length; i ++) {
				childrenStatus[i] = 0;
			}
			
			/* Assigns children status to all centroids to 1 if it have at least 
			   1 instance in them. */
			for (int i = 0; i < clusterAssignment.length; i ++) {
				childrenStatus[clusterAssignment[i]] = 1;
			}
			
			// Solving for Orphaned centroids (meaning no instances in them)
			for(int i = 0; i < childrenStatus.length; i ++) {
				if(childrenStatus[i] == 0) {
					double furthestDistance = -1;
					int nearestCentroid = -1;
					
					/* Look for the furthest distant instance to its respective centroid
					   and use it as the new position for the orphaned centroid. */
					for (int j = 0; j < instances.length; j ++) {
						Double distance = calcDistance(instances[j], centroids[clusterAssignment[j]]);
						if(furthestDistance < 0 || furthestDistance <= distance) {
							if(furthestDistance == distance) {
								//smaller cluster id
								if(store2 > -1 && clusterAssignment[j] < clusterAssignment[store2]) {
									furthestDistance = distance;
									nearestCentroid = j;
									store2 = j;
								}
							} else {
								furthestDistance = distance;
								nearestCentroid = j;
								store2 = j;
							}
						}
					}
					clusterAssignment[nearestCentroid] = i;
					
					// Reallocate clusters after checking for orphaned centroids.
					
					for (int k = 0; k < instances.length; k++) {
						double[] instanceFeatures = instances[k];
						double nearDistance = -1;
						int nearCentroid = -1;
						double[] centFeatures;
						for (int l = 0; l < centroids.length; l++) {
							centFeatures = centroids[l];
							Double distance = calcDistance(centFeatures, instanceFeatures);
							if(distance == nearDistance){
								// smaller id
								if(store3 > -1 && clusterAssignment[l] < clusterAssignment[store3]) {
									nearDistance = distance;
									nearCentroid = l;
									store3 = l;
								}
							} else if(nearDistance < 0 || distance <= nearDistance){
								nearDistance = distance;
								nearCentroid = l;
								store3 = l;
							}
						}

						clusterAssignment[k] = nearCentroid;
					}
					
				}
			}
			
			// Updating centroid's coordinates
			for (int i = 0; i < centroids.length; i ++) {
				double[] total = new double[numberOfDimensions];
				int totalNumber = 0;
				for (int j = 0; j < clusterAssignment.length; j ++) {
					// Checking if they belong to the same cluster
					if(clusterAssignment[j] == i) {
						for(int k = 0; k < instances[k].length; k ++) {
							total[k] += instances[j][k];
						}
						totalNumber ++;
					}
				}
				// Setting new position for centroid
				for (int j = 0; j < total.length; j ++) {
					centroids[i][j] = total[j]/totalNumber;
				}
			}

			// Calculate Distortion as the sum of square of the distance
			double initDistortion = 0;
			for (int i = 0; i < instances.length; i++) {
				initDistortion += Math.pow(calcDistance(instances[i], 
						centroids[clusterAssignment[i]]), 2);
			}
			
			distortionList.add(initDistortion);
			
			// Check to see if need to continue or terminate if
			// |[distortion(i) - distortion(i-1)]/distortion(i-1)|<threshold
			if(loopItr > 0 && Math.abs((distortionList.get(loopItr)
                    - distortionList.get(loopItr - 1))
                    /distortionList.get(loopItr - 1)) < threshold) {
				break;
			} else {
				continue;
			}
               
			
		}
		
		// Initialize the result to be returned as a KMeansResult object
		KMeansResult finalResult = new KMeansResult();
		finalResult.centroids = centroids;
		finalResult.clusterAssignment = clusterAssignment;
		finalResult.distortionIterations = new double[distortionList.size()];
		
		// Place the distortionList into distortionIterations array
		for (int j = 0; j < finalResult.distortionIterations.length; j++) {
			finalResult.distortionIterations[j] = distortionList.get(j);
		}

		return finalResult;
	}

	
	/**
     * This method calculates the Euclidean distance between
     * 2 point.
     * 
     * @param point1
     *            array of the first point
     * @param point2
     *            array of the second point
     * @return The Euclidean distance the 2 points
     */
	
	public Double calcDistance(double[] point1, double[] point2){
		double distance = 0;
		for (int i = 0; i < point1.length; i++) {
			distance += (point1[i] - point2[i]) * (point1[i] - point2[i]);
		}
		distance = Math.sqrt(distance);
		return distance;
	}

}