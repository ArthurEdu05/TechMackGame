package br.techmackgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Level2 extends Level {

    public Level2(FitViewport viewport, SpriteBatch spriteBatch) {
        super(viewport, spriteBatch);
        System.out.println("Level 2 iniciado!");
    }

    @Override
    protected void setupBackground() {
        backgroundTexture = new Texture("backgroundlvl2.png");
        bgX1 = 0;
        bgX2 = bgWidthUnits;
        bgSpeed = 3.5f; // fundo se move mais rápido
    }

    @Override
    protected void setupObjects() {
        // energia para este nível
        energy = new Energy(20f, 100f);
        // player
        Texture playerTexture = new Texture("standingRight.png");
        player = new Player(playerTexture, 1, 1, 0.5f, 1f, viewport);
        player.setEnergy(energy);
        
        

        // caminhão
        float truckWidth = 4f;
        float truckHeight = 2f;
        float truckX = viewport.getWorldWidth() - truckWidth / 2;
        float truckY = 1f;
        truck = new Truck(truckX, truckY, truckWidth, truckHeight);

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
        return 2f; // spawn mais rapido
    }

    @Override
    protected String getIntroText() {
        return "Estamos chegando na cidade, mas seus pertences não param de cair! Não pare de recolher seus pertences e mantenha sua energia!";
    }

    @Override
    public Music getIntroSound() {
        return Gdx.audio.newMusic(Gdx.files.internal("intro2Sound.mp3"));
    }

    @Override
    protected int getRequiredScore() {
        return 50;  // pontos necessários para completar
    }
}