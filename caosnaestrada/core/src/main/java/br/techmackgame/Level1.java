package br.techmackgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Level1 extends Level {

    public Level1(FitViewport viewport, SpriteBatch spriteBatch) {
        super(viewport, spriteBatch);
        this.bgSpeed = 2f;         // Velocidade do fundo
    }

    @Override
    protected float getSpawnInterval() {
        return 4f;
    }

    @Override
    protected void setupBackground() {
        backgroundTexture = new Texture("backgroundlvl1.png");
        bgWidthUnits = 8f; 
        bgX1 = 0;
        bgX2 = bgWidthUnits;
    }

    @Override
    protected void setupObjects() {

        // energia para este nível
        float maxEnergy = 100f; // máximo
        float baseEnergy = maxEnergy * 0.1f; // começa com 50% 
        energy = new Energy(baseEnergy, maxEnergy);

        // Player (depende da energia)
        Texture playerTexture = new Texture("standingRight.png");
        player = new Player(playerTexture, 1, 1, 0.5f, 1f, viewport);
        player.setEnergy(energy);

        // Caminhão
        float truckWidth = 4f;
        float truckHeight = 2f;
        float truckX = viewport.getWorldWidth() - truckWidth / 2;
        float truckY = 1f;
        truck = new Truck(truckX, truckY, truckWidth, truckHeight);

        // Objetos que caem 
        objectTextures = new Array<>();
        objectTextures.add(new Texture("abajur.png"));
        objectTextures.add(new Texture("brinquedos.png"));
        objectTextures.add(new Texture("notebook.png"));
        objectTextures.add(new Texture("roupas.png"));
        objectTextures.add(new Texture("travesseiro.png"));
    }

    @Override
    protected String getIntroText() {
        return "Seu caminhão de mudança está partindo, mas a porta traseira se abriu no meio da estrada! Recolha seus pertences e mantenha sua energia!";
    }
}