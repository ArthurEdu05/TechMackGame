package br.techmackgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Level3 extends Level {

    public Level3(FitViewport viewport, SpriteBatch spriteBatch) {
        super(viewport, spriteBatch);
        System.out.println("Level 3 iniciado!");
    }

    @Override
    protected void setupBackground() {
        backgroundTexture = new Texture("backgroundlvl3.png");
        bgX1 = 0;
        bgX2 = bgWidthUnits;
        bgSpeed = 5f; // fundo se move mais rápido
    }

    @Override
    protected void setupObjects() {
        // player
        Texture playerTexture = new Texture("standingRight.png");
        player = new Player(playerTexture, 1, 0.5f, 0.5f, 1f, viewport);
        // energia para este nível
        energy = new Energy(10f, 150f);

        // caminhão
        float truckWidth = 4f;
        float truckHeight = 2f;
        float truckX = viewport.getWorldWidth() - truckWidth / 2;
        float truckY = 1f;
        truck = new Truck(truckX, truckY - 0.5f, truckWidth, truckHeight);

        // objetos aleatórios
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

    @Override
    public Music getIntroSound() {
        return Gdx.audio.newMusic(Gdx.files.internal("intro3Sound.mp3"));
    }

    @Override
    protected int getRequiredScore() {
        return 40;  // pontos necessários para completar
    }
}