/**
 * AI_music_generator
 * Created by Sergey on 2017-10-27
 */
public class Particle1 implements IParticle {

    public final int CHORDS_NUMBER = 16;

    private final int MIN_TONE = 48; // Midi note can't be lower than that
    private final int BORDER_TONE = 72;  // It's better for note to be lower than that
    private final int MAX_TONE = 96; // Midi note can't be higher than that

    private final double INERTIA_COMPONENT = 1; // Tendency to save current velocity
    private final double COGNITIVE_COMPONENT = 1; // Tendency to return to local best
    private final double SOCIAL_COMPONENT = 1; // Tendency to return to global best

    private MyChord[] chords = new MyChord[CHORDS_NUMBER];
    private MyVector3[] velocities = new MyVector3[CHORDS_NUMBER];
    private double fitness;

    private MyChord[] bestChords;
    private double bestFitness;

    public Particle1() {
        regenerate();
    }

    public Particle1(MyChord[] chords, MyVector3[] velocities, double fitness) {
        this.chords = chords;
        bestChords = chords;

        this.velocities = velocities;

        this.fitness = fitness;
        bestFitness = fitness;
    }

    public void regenerate() {
        int vecDeltaAbs = (MAX_TONE - MIN_TONE) / 2;

        for(int i = 0; i < CHORDS_NUMBER; i++) {
            chords[i] = new MyChord(MIN_TONE, MAX_TONE);
            velocities[i] = new MyVector3(-vecDeltaAbs, vecDeltaAbs);
        }

        calculateFitness();

        bestChords = chords.clone();
        bestFitness = fitness;
    }

    @Override
    public double calculateFitness() {
        fitness = 0;
        // TODO: fitness calculation

        return fitness;
    }

    public void setVelocities(MyVector3[] velocities) {
        this.velocities = velocities;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public void updateVelocity(IParticle gBest) {
        Particle1 gBestParticle = (Particle1)gBest;

        for(int i = 0; i < CHORDS_NUMBER; i++) {
            MyVector3 component1 = velocities[i].mul(INERTIA_COMPONENT);
            MyVector3 component2 = bestChords[i].sub(chords[i]).toVector().mul(COGNITIVE_COMPONENT * Randomizer.getRandomFactor());
            MyVector3 component3 = gBestParticle.getChord(i).sub(chords[i]).toVector().mul(SOCIAL_COMPONENT * Randomizer.getRandomFactor());

            velocities[i] = component1.add(component2).add(component3);
        }
    }

    @Override
    public void updateParticle() {
        for(int i = 0; i < CHORDS_NUMBER; i++) {
            chords[i] = chords[i].add(velocities[i]);
        }

        calculateFitness();

        if(fitness >= bestFitness) {
            bestChords = chords.clone();
            bestFitness = fitness;
        }
    }

    @Override
    public IParticle clone() {
        return new Particle1(chords.clone(), velocities.clone(), fitness);
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public MyChord[] getChords() {
        return chords;
    }

    public MyChord getChord(int ind) {
        return chords[ind];
    }

}
