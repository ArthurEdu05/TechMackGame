package br.techmackgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/// Esta é a classe base. Ela define as propriedades e comportamentos que são comuns a todos os objetos do nosso jogo
/// Atributos: Sprite é a representação visual do objeto. Rectangle é a área de colisão do objeto.


public abstract class GameObject {
    protected Sprite objectSprite;
    protected Rectangle bounds;
    protected float speed;
    protected float arc;

    public GameObject(Texture texture, float x, float y, float width, float height) {
        this.objectSprite = new Sprite(texture);
        this.objectSprite.setSize(width, height);
        this.objectSprite.setPosition(x, y);
        this.speed = 2f;
        this.bounds = new Rectangle(x, y, width, height);
        //this.arc = 2f;
    }

    // metodo abstrato para que as classes filhas definam sua própria lógica de atualização.
    public abstract void update(float delta);

    public void draw(SpriteBatch batch) {
        objectSprite.draw(batch);
    }

    public Rectangle getBounds() {
        // Atualiza a posição do retângulo de colisão para coincidir com a do sprite
        bounds.setPosition(objectSprite.getX(), objectSprite.getY());
        return bounds;
    }

    public void setPosition(float x, float y) {
        objectSprite.setPosition(x, y);
    }

    public void translate(float dx, float dy) {
        objectSprite.translate(dx, dy);
        //atualiza a hitbox apos movimento
        bounds.setPosition(objectSprite.getX(), objectSprite.getY());
    }

    public float getX() { return objectSprite.getX(); }
    public float getY() { return objectSprite.getY(); }
    public float getWidth() { return objectSprite.getWidth(); }
    public float getHeight() { return objectSprite.getHeight(); }
    public Sprite getSprite() {
        return objectSprite;
    }
}

