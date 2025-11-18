package br.techmackgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Energy {

    // valores de energia: 
    private float energy; // atual
    private float maxEnergy;
    private float baseEnergy;

    // intensidade da pedalada 
    private float pedalPower = 0f;
 
    private long lastAPress = 0L;
    private long lastDPress = 0L;

    // parâmetros para converter tempo entre presses em intensidade
    private final float minInterval = 0.05f; 
    private final float maxInterval = 1.0f;  
    private final float intensityScale = 0.6f; 

    // ajustáveis por nivel?
    private final float pedalMax = 10f;       
    private final float pedalDecay = 2.5f;    

    // valor convertido em energia por segundo
    private final float energyGainPerPower = 0.4f;

    // decaimento da energia por segundo 
    private final float passiveDecayRate = 1.2f;

    public Energy(float baseEnergy, float maxEnergy) {
        this.baseEnergy = baseEnergy;
        this.maxEnergy = maxEnergy;
        this.energy = baseEnergy;
    }

    //reseta energia ao iniciar um novo nivel.
    public void resetForLevel(float baseEnergy, float maxEnergy) {
        this.baseEnergy = baseEnergy;
        this.maxEnergy = maxEnergy;
        this.energy = baseEnergy;
        this.pedalPower = 0f;
    }

    // converte a pedalada em energia 
     
    public void update(float delta) {
        // usa as teclas A e D para simular pedaladas
        boolean aJust = Gdx.input.isKeyJustPressed(Input.Keys.A);
        boolean dJust = Gdx.input.isKeyJustPressed(Input.Keys.D);

        // Se A ou D foram pressionadas, calcula a intensidade baseada no tempo entre essa e a última pressionada da mesma tecla.
        if (aJust) {
            long now = TimeUtils.millis();
            float intensity = 0.5f; // valor base caso seja a primeira pressão
            if (lastAPress != 0L) {
                float interval = MathUtils.clamp((now - lastAPress) / 1000f, minInterval, maxInterval);
                intensity = MathUtils.clamp(intensityScale / interval, 0.1f, 3f);
            }
            lastAPress = now;
            pedal(intensity);
        }

        if (dJust) {
            long now = TimeUtils.millis();
            float intensity = 0.5f;
            if (lastDPress != 0L) {
                float interval = MathUtils.clamp((now - lastDPress) / 1000f, minInterval, maxInterval);
                intensity = MathUtils.clamp(intensityScale / interval, 0.1f, 3f);
            }
            lastDPress = now;
            pedal(intensity);
        }

        // converte pedalPower em ganho de energia
        if (pedalPower > 0f) {
            float gain = pedalPower * energyGainPerPower * delta;
            energy = Math.min(maxEnergy, energy + gain);
        }

        // decaimento da energia
        energy = Math.max(0f, energy - passiveDecayRate * delta);
        pedalPower = Math.max(0f, pedalPower - pedalDecay * delta);
    }

    public void pedal(float intensity) {
        if (intensity <= 0f) return;
        float add = intensity * 4f; 
        pedalPower = Math.min(pedalMax, pedalPower + add);
    }

    public float getEnergy() {
        return energy;
    }

    public float getMaxEnergy() {
        return maxEnergy;
    }

    public float getPedalPower() {
        return pedalPower;
    }

    // public void addEnergy(float amount) {
    //     energy = Math.min(maxEnergy, energy + amount);
    // }
}
