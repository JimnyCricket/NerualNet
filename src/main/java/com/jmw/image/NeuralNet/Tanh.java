package com.jmw.image.NeuralNet;

/**
 * Tanh Activation Function
 * @author Jimmy
 *
 */
public class Tanh extends ActivationFunction
{
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 */
	private static final long serialVersionUID = -9163490316545429762L;

	/**
	 * Constructs new Tanh Activation Function
	 */
	public Tanh() {
	}

	@Override
	final public double getOutput(double net) {
		// conditional logic helps to avoid overflow and underflow
		if (net > 100) {
			return 1.0;
		}else if (net < -100) {
			return -1.0;
		}

		double E_x = Math.exp(2.0 * net);                
		return (E_x - 1.0) / (E_x + 1.0);
	}

	@Override
	final public double getDerivative(double net)
	{
		double output = getOutput(net);
		return (1.0 - output * output);
	}

	@Override
	public String getType()
	{
		return "Tanh";
	}	

}

