/**
 * Esta classe representa os pertences que caem do caminhão. 
 * Ela estende GameObject para herdar posição e renderização, mas adiciona:
 * - Física de projétil (gravidade e arremesso).
 * - Rotação visual.
 * - Detecção de colisão com o chão.
 */


package br.techmackgame;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class FallingObject extends GameObject {

    private float velocityX;
    private float velocityY;
    private float gravity = -9.8f; // aceleração para baixo
    private boolean active = true;
    private float groundY = 1f; // altura do chão onde o objeto é destruido
    private float rotationSpeed; // velocidade de rotação 

    // controle de pontuação e coleta
    private int points; 
    private boolean collected = false; 
    private Sound impactSound;
    private boolean hasPlayedImpactSound = false;
    private boolean hitGround = false;

    public FallingObject(Texture texture, float startX, float startY, float width, float height) {
        super(texture, startX, startY, width, height);
        reset(startX, startY);
    }

    // novo arco aleatório a partir da posição inicial (caminhão)
    public void reset(float startX, float startY) {
        objectSprite.setPosition(startX, startY);
        bounds.setPosition(startX, startY);
        active = true;
        collected = false;
        hitGround = false;
        hasPlayedImpactSound = false;

        // arco aleatório
        velocityX = MathUtils.random(-4f, -1f); // horizontal para a esquerda
        velocityY = MathUtils.random(2.5f, 4f); // vertical para cima

        // gira em sentido horário ou anti-horário aleatoriamente
        rotationSpeed = MathUtils.randomSign() * MathUtils.random(90f, 360f);
        objectSprite.setOriginCenter(); // Garante que gire em torno do próprio eixo
    }

    /*
     * Executado a cada frame. Aplica as leis da física simples:
     * - A gravidade reduz a velocidade vertical constantemente.
     * - A posição é atualizada baseada na velocidade atual.
     * - A rotação é aplicada.
     * - Verifica colisão com o chão.
     */
    @Override
    public void update(float delta) {
        if (!active) return;

        
        velocityY += gravity * delta;
        translate(velocityX * delta, velocityY * delta);

        // atualiza a rotação
        float newRotation = objectSprite.getRotation() + rotationSpeed * delta;
        objectSprite.setRotation(newRotation);

        // caiu no chão destroi
        if (objectSprite.getY() <= groundY) {
            if (!hasPlayedImpactSound && impactSound != null) {
                float volume = 0.5f;
                float pitch = 0.9f + MathUtils.random() * 0.2f;
                impactSound.play(volume, pitch, 0f);
                hasPlayedImpactSound = true;
            }
            hitGround = true;
            active = false;
        }
    }

    public void draw(SpriteBatch batch) {
        if (active) super.draw(batch);
    }

    public boolean isActive() {
        return active;
    }

    public void collect() {
        // marca como coletado e desativa o objeto
        active = false;
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    public void deactivate() {
    active = false;
    collected = false;
    hitGround = false;
    }

    public Texture getTexture() {
        return objectSprite.getTexture();
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setImpactSound(Sound sound) {
        this.impactSound = sound;
    }

    public boolean hasHitGround() {
        return hitGround;
    }
}
