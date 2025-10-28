package br.techmackgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Truck extends GameObject {

    private Animation<TextureRegion> animation;
    private float stateTime;
    private boolean active;
    private TextureRegion[] framesTruck;
    private Animation<TextureRegion> runLoopAnimation; // frames 1..8 em loop

    public Truck(float x, float y, float width, float height) {
        // cria temporariamente uma Texture a partir de RunTruck1 para passar ao super; a região será substituída em seguida
        super(new Texture("RunTruck1.png"), x, y, width, height);

        // carrega os frames RunTruck1..RunTruck8
        framesTruck = new TextureRegion[8];
        for (int i = 1; i <= 8; i++) {
            Texture t = new Texture("RunTruck" + i + ".png");
            framesTruck[i - 1] = new TextureRegion(t);
        }

        // cria a animação de loop com os frames 1..8 (já em framesTruck)
        runLoopAnimation = new Animation<>(0.1f, framesTruck);

        // começa exibindo o primeiro frame do loop (RunTruck1)
        objectSprite.setRegion(framesTruck[0]);

        this.animation = runLoopAnimation;
        this.stateTime = 0f;
        this.active = true;
    }

    public Truck(Animation<TextureRegion> animation, float x, float y, float width, float height) {
        super(animation.getKeyFrame(0).getTexture(), x, y, width, height);
        this.animation = animation;
        this.stateTime = 0f;
        this.active = true;
    }

    @Override
    public void update(float delta) {
        if (!active) return;

        stateTime += delta;

        // Faz o loop contínuo dos frames 1..8
        TextureRegion frame = runLoopAnimation.getKeyFrame(stateTime, true);
        objectSprite.setRegion(frame);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public float getWidthUnits() {
        return objectSprite.getWidth();
    }

    public float getHeightUnits() {
        return objectSprite.getHeight();
    }
}
