package br.techmackgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Level2 extends Level {

    public Level2(FitViewport viewport, SpriteBatch spriteBatch) {
        super(viewport, spriteBatch);
        System.out.println("🎮 Level 2 iniciado!");
    }

    @Override
    protected void setupBackground() {
        backgroundTexture = new Texture("backgroundlvl2.png");
        bgX1 = 0;
        bgX2 = bgWidthUnits;
        bgSpeed = 3.5f; // Fundo se move mais rápido
    }

    @Override
    protected void setupObjects() {
        // Player
        Texture playerTexture = new Texture("standingRight.png");
        player = new Player(playerTexture, 1, 1, 0.5f, 1f, viewport);
    // energia para este nível
    energy = new Energy(15f, 120f);

        // Caminhão
        float truckWidth = 4f;
        float truckHeight = 2f;
        float truckX = viewport.getWorldWidth() - truckWidth / 2;
        float truckY = 1f;
        truck = new Truck(truckX, truckY, truckWidth, truckHeight);

        // Objetos aleatórios
        objectTextures = new Array<>();
        objectTextures.add(new Texture("abajur.png"));
        objectTextures.add(new Texture("brinquedos.png"));
        objectTextures.add(new Texture("notebook.png"));
        objectTextures.add(new Texture("roupas.png"));
        objectTextures.add(new Texture("travesseiro.png"));
    }

    @Override
    protected float getSpawnInterval() {
        return 2f; // spawn mais rapido
    }

    @Override
    protected String getIntroText() {
        return "Estamos chegando na cidade, mas seus pertences não param de cair! Não pare de recolher seus pertences e mantenha sua energia!";
    }
}