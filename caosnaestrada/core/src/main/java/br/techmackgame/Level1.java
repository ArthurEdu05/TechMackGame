package br.techmackgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Level1 extends Level {

    public Level1(FitViewport viewport, SpriteBatch spriteBatch) {
        super(viewport, spriteBatch);
        this.bgSpeed = 2f;         // Velocidade do fundo
        this.spawnInterval = 3f;   // Intervalo inicial de spawn
    }

    @Override
    protected void setupBackground() {
        backgroundTexture = new Texture("backgroundlvl1.png");
        bgWidthUnits = 8f; // mesma proporção da sua Main
        bgX1 = 0;
        bgX2 = bgWidthUnits;
    }

    @Override
    protected void setupObjects() {
        // Player
        Texture playerTexture = new Texture("standingRight.png");
        player = new Player(playerTexture, 1, 1, 0.5f, 1f, viewport);

        // Caminhão
        float truckWidth = 4f;
        float truckHeight = 2f;
        float truckX = viewport.getWorldWidth() - truckWidth / 2;
        float truckY = 1f;
        truck = new Truck(truckX, truckY, truckWidth, truckHeight);

        // Objetos que caem (iguais aos da sua Main)
        objectTextures = new Array<>();
        objectTextures.add(new Texture("abajur.png"));
        objectTextures.add(new Texture("brinquedos.png"));
        objectTextures.add(new Texture("notebook.png"));
        objectTextures.add(new Texture("roupas.png"));
        objectTextures.add(new Texture("travesseiro.png"));
    }
}