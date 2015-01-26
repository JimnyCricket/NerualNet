package com.jmw.image.NeuralNet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mnist.MnistManager.MnistManager;

/**
 * NeuralNet Implementation
 * App.java
 * Purpose: Main class to run a Neural Network Tests.
 * 
 * @author Jimmy Whitaker
 * @version 0.1 1/21/15
 */
public class App 
{
	/**
	 * The main method for the NeuralNet program.
	 *
	 * @param args Not used
	 */
	public static void main( String[] args )
	{
		//Compute Runtime
		long start = System.currentTimeMillis();

		//Run test
		newMnistTest();

		System.out.println("Runtime: " + (System.currentTimeMillis()-start));
	}

	/**
	 * Mnist Test
	 */
	public static void newMnistTest()
	{
		int[] neuronsPerLayer = {300,10};
		String[] layerType = {"Relu","Softmax"}; // Currently all neurons in a layer have the same activation function
		int epochs = 1000;
		int batchSize = 80;
		double learningRate = 0.1;
		double momentum = 1;

		// Import training data
		MnistDataset mnistTrainingDataset = new MnistDataset("train-images-idx3-ubyte","train-labels-idx1-ubyte");

		NeuralNet nn = new NeuralNet(neuronsPerLayer, layerType, mnistTrainingDataset.getNumFeatures());

		//Import testing data
		MnistDataset mnistTestingDataset = new MnistDataset("t10k-images-idx3-ubyte","t10k-labels-idx1-ubyte");
		
		//Train the classifier
		nn.train(mnistTrainingDataset, mnistTestingDataset, epochs, batchSize, learningRate, momentum);

		//Save NeuralNet after training
		nn.save("NeuralNet2.ser");		
	}
	
	public static void continueMnistTest()
	{
		int epochs = 100;
		int batchSize = 80;
		double learningRate = 0.1;
		double momentum = 1;
		
		//Import Training Data
		MnistDataset mnistTrainingDataset = new MnistDataset("train-images-idx3-ubyte","train-labels-idx1-ubyte");
		
		//Load saved NeuralNet
		NeuralNet nn = NeuralNet.load("NeuralNet.ser");
		System.out.println("NumberOfNeurons: " + nn.layers[0].getWeight().getRows());
		
		//Test
		MnistDataset mnistTestingDataset = new MnistDataset("t10k-images-idx3-ubyte","t10k-labels-idx1-ubyte");
		
		//Continue Training
		nn.train(mnistTrainingDataset, mnistTestingDataset, epochs, batchSize, learningRate, momentum);
		
		
		
	}

	/**
	 * XOR Test
	 */
	public static void xorTest()
	{
		int[] neuronsPerLayer = {2,1};
		String[] layerType = {"Tanh","Tanh"}; // Currently all neurons in a layer have the same activation function
		int epochs = 10000;
		int batchSize = 2;
		double learningRate = 0.7;
		double momentum = 1.0;

		double[][] xor_data = {{0,0,1,1},{0,1,0,1}};
		double[] xor_labels = {0,1,1,0};

		MatxDataset dataset = new MatxDataset(Matx.createMatx(xor_data).getTranspose(), Matx.createMatx(xor_labels));
		//TODO add mean and stddev to parameters
		NeuralNet nn = new NeuralNet(neuronsPerLayer, layerType, dataset.getData().getCols()); 
		
		nn.train(dataset, dataset, epochs, batchSize, learningRate, momentum);
	}
	
	/**
	 * Manual Back-propagation Test
	 */
	public static void backpropTest()
	{
		//Error Discovery Test - Comparison by Hand
		int[] neuronsPerLayer = {2,1};
		String[] layerType = {"Sigmoid","Sigmoid"};
		double[] data = {1,0,1};
		double[] label = {1};
		MatxDataset dataset = new MatxDataset(Matx.createMatx(data).getTranspose(), Matx.createMatx(label));
		NeuralNet nn = new NeuralNet(neuronsPerLayer, layerType, dataset.getData().getCols());
		double[][] w1 = {{0.2,0.4,-0.5},{-0.3,0.1,0.2}};
		double[] w2 = {-0.3,-0.2};
		nn.layers[0].weight = Matx.createMatx(w1);
		nn.layers[1].weight = Matx.createMatx(w2).getTranspose();
		double[][] b1 = {{-0.4},{0.2}};
		double[][] b2 = {{0.1}};
		nn.layers[0].bias = Matx.createMatx(b1);
		nn.layers[1].bias = Matx.createMatx(b2);
		nn.train(dataset,dataset, 1, 1, 0.9, 1);
//		nn.test(dataset,false);
	}
}
