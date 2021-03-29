import synthesizer.GuitarString;

/** A client that uses the synthesizer package to replicate a plucked guitar string sound */
public class GuitarHero {

    private static final double CONCERT = 440.0;

    public static void main(String[] args) {

        synthesizer.GuitarString[] strings = new GuitarString[37];
        for (int i = 0; i < 37; i++) {
            strings[i] = new GuitarString(CONCERT * Math.pow(2, (i - 24.0) / 12.0));
//            strings[i] = new GuitarString(CONCERT * Math.pow(2, (i - 24.0) / 12.0) * 2);
//            natural resonance frequency
        }

        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

        while (true) {

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int keyIndex = keyboard.indexOf(key);
                if (keyIndex > -1) {
                    strings[keyIndex].pluck();
                }
            }

            double sample = 0.0;
            for (GuitarString s: strings) {
                sample += s.sample();
            }

        /* play the sample on standard audio */
            StdAudio.play(sample);

        /* advance the simulation of each guitar string by one step */
            for (GuitarString s: strings) {
                s.tic();
            }
        }
    }
}

