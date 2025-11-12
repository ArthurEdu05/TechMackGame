package br.techmackgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Level3 extends Level {

    public Level3(FitViewport viewport, SpriteBatch spriteBatch) {
        super(viewport, spriteBatch);
        System.out.println("🎮 Level 3 iniciado!");
    }

    @Override
    protected void setupBackground() {
        backgroundTexture = new Texture("backgroundlvl3.png");
        bgX1 = 0;
        bgX2 = bgWidthUnits;
        bgSpeed = 5f; // Fundo se move mais rápido
    }

    @Override
    protected void setupObjects() {
        // Player
        Texture playerTexture = new Texture("standingRight.png");
        player = new Player(playerTexture, 1, 1, 0.5f, 1f, viewport);
    // energia para este nível
    energy = new Energy(10f, 150f);

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
        return 0.1f; // spawn frenético
    }

    @Override
    protected String getIntroText() {
        return "Chegamos na cidade, e os pertences continuam ainda caindo! Continue recolhendo seus pertences e mantenha sua energia!";
    }
}