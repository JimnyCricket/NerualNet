package com.jmw.image.NeuralNet;

/**
 * Neural Network Layer
 * 
 * @author Jimmy Whitaker
 *
 */
public class Layer
{
	private Matx weightedInput; // weight*layerInput
	private Matx output;
	public Matx weight;
	private Matx error; // error delta
	public Matx bias;
	
	// TODO create mask matrix for drop connect
	private ActivationFunction activationFunction;
	
	/**
	 * Constructs a new Layer for a Neural Network.
	 * 
	 * @param activationFunction String representation of an activation function.
	 * @param numNeurons Number of neurons in the layer
	 * @param numInputs Number of inputs to the layer
	 */
	public Layer(String activationFunction, int numNeurons, int numInputs)
	{		
		if(activationFunction.equals("Sigmoid"))
		{
			this.activationFunction = new Sigmoid();
		} else if(activationFunction.equals("Tanh"))
		{
			this.activationFunction = new Tanh();
		}else if(activationFunction.equals("Relu"))
		{
			this.activationFunction = new Relu();
		}
		
		//Randomly initialize the weight Matx according to a normal distribution
		this.weight = Matx.createRandNormMatx(numNeurons, numInputs);
		
	}
	
	/**
	 * Compute the output of the layer given an input.
	 * 
	 * @param layerInput
	 * @return output of the layer
	 */
	public Matx computeLayerOutput(Matx layerInput)
	{
		Matx input = Matx.multiply(weight, layerInput);
		if(bias != null)
		{
			input = Matx.add(input, bias);
			
		}
		this.weightedInput = input;
		this.output = activationFunction.getOutput(input);
		return output;
	}

	/**
	 * Compute error delta for output layer.
	 * Store value in layer and return it.
	 * 
	 * @param dataLabel
	 * @return error
	 */
	public Matx computeErrorDelta(Matx dataLabel)
	{
		Matx term1 = Matx.subtract(dataLabel,output);
		Matx term2 = activationFunction.getDerivative(weightedInput);
		this.error =  Matx.elementMultiply(term2, term1);
		return error;
	}

	/**
	 * Compute error delta for hidden layer.
	 * Store value in layer and return it.
	 * 
	 * @param nextLayerError
	 * @param nextLayerWeight
	 * @return error
	 */
	public Matx computeErrorDelta(Matx nextLayerError, Matx nextLayerWeight)
	{
		Matx term1 = Matx.multiply(nextLayerWeight.getTranspose(),nextLayerError);
		Matx term2 = activationFunction.getDerivative(this.weightedInput);
		this.error = Matx.elementMultiply(term1, term2);
		return error;
	}
	
	/**
	 * Update the weight and bias of the layer.
	 * 
	 * @param learningRate 
	 * @param prevLayerOutput
	 */
	public void updateWeights(double learningRate, Matx prevLayerOutput)
	{
		Matx deltaWeight = Matx.multiply(this.error, prevLayerOutput.getTranspose());
		deltaWeight = Matx.scalarMultiply(learningRate, deltaWeight);
		this.weight = Matx.add(this.weight, deltaWeight);
		
		//Update the bias if there is one. 
		if(this.bias != null)
		{
			this.bias = Matx.add(this.bias, Matx.scalarMultiply(learningRate, this.error));
		}
	}
	
	/**
	 * @return current weight of the layer
	 */
	public Matx getWeight()
	{
		return this.weight;
	}

	/**
	 * @return current output of the layer
	 */
	public Matx getOutput()
	{
		return this.output;
	}
	
}