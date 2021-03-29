package synthesizer;

//Make sure this class is public
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor
//    private static final double DECAY = .998; // improve the realism
//    private static final double DECAY = 1.0; // better drum sound

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<>(capacity);
        while (!buffer.isFull()) {
            buffer.enqueue(0.0);
        }
    }

    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        //       Make sure that your random numbers are different from each other.
        while (!buffer.isEmpty()) {
            buffer.dequeue();
        }
        while (!buffer.isFull()) {
            double r = Math.random() - 0.5;
            buffer.enqueue(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double frontSample = buffer.dequeue();
        double newSample = (frontSample + buffer.peek()) * 0.5 * DECAY;
//        newSample = -newSample; // change the sound from guitar-like to harp-like
//        if (Math.random() > 0.5) {
//            newSample = -newSample; // drum sound
//        }
        buffer.enqueue(newSample);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }

//    @Override
//    public String toString() {
//        StringBuilder returnSB = new StringBuilder("{");
//        BoundedQueue<Double> temp = new ArrayRingBuffer<>(buffer.capacity());
//        while (!buffer.isEmpty()) {
//            double sample = buffer.dequeue();
//            temp.enqueue(sample);
//            returnSB.append(sample);
//            returnSB.append(" ");
//        }
//        returnSB.append("}");
//        buffer = temp;
//        return returnSB.toString();
//    }
}
