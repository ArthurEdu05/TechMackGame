package br.techmackgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;


//Essa classe representa a nave. Ela herda de GameObject e implementa a lógica de movimento do jogador, lendo as entradas do teclado.
public class Player extends GameObject{
    private Vector2 touchPos;
    private Viewport viewport;
    // private Texture standingLeft;
    // private Texture standingRight;

    // animações de corrida 
    private Animation<TextureRegion> runAnimationLeft;
    private Animation<TextureRegion> runAnimationRight;
    TextureRegion frameAtual;
    private float stateTime = 0f;
    private Texture[] runRightTextures;
    private float originalWidth;
    private float originalHeight;
   
    // controles de pedal
    private TextureRegion standingRegion;
    private TextureRegion standingLeftRegion;
    private int lastPedal = 0; // 1 = LEFT, 2 = RIGHT
    private int facing = 1; // 1 = right, -1 = left (começa direita right)
    private float pedalPower = 0f;
    private final float pedalInc = 2f;
    private final float pedalMax = 6f;
    private final float pedalDecay = 3f;
    
    // velocidade fixa do jogador 
    private final float moveSpeed = 4f;

    // variaveis de energia pra poder se mexer


    public Player(Texture texture, float x, float y, float width, float height, Viewport viewport) {
        super(texture, x, y, width, height);
        this.viewport = viewport;
        this.touchPos = new Vector2();
        
        // armazena o tamanho original da sprite 
        this.originalWidth = objectSprite.getWidth();
        this.originalHeight = objectSprite.getHeight();

        // quando o personagem tá parado ---- arrumar pra ele ficar parado pra esquerda dependendo da direçao
        this.standingRegion = new TextureRegion(texture);
        // cria uma versão espelhada para quando estiver voltado para a esquerda
        this.standingLeftRegion = new TextureRegion(texture);
        this.standingLeftRegion.flip(true, false);

        // frames de corrida (direita) 
        runRightTextures = new Texture[6];
        TextureRegion[] rightRegions = new TextureRegion[6];
        for (int i = 0; i < 6; i++){
            runRightTextures[i] = new Texture("Run" + (i+1) + ".png");
            rightRegions[i] = new TextureRegion(runRightTextures[i]);
        }
        runAnimationRight = new Animation<>(0.1f, rightRegions);
        frameAtual = runAnimationRight.getKeyFrame(stateTime, true);
        objectSprite.setRegion(frameAtual);


        // animação da esquerda espelhando as da direita
        TextureRegion[] leftRegions = new TextureRegion[6];
        for (int i = 0; i < 6; i++){
            leftRegions[i] = new TextureRegion(rightRegions[i]);
            leftRegions[i].flip(true, false);
        }
        runAnimationLeft = new Animation<>(0.1f, leftRegions);
        frameAtual = runAnimationRight.getKeyFrame(stateTime, true);
        objectSprite.setRegion(frameAtual);

    }

    @Override
    public void update(float delta) {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float playerWidth = objectSprite.getWidth();
        float playerHeight = objectSprite.getHeight();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){ 
            // correr direita
            objectSprite.translateX(moveSpeed * delta);
            stateTime += delta;
            frameAtual = runAnimationRight.getKeyFrame(stateTime, true);
            objectSprite.setRegion(frameAtual);
            objectSprite.setSize(originalWidth, originalHeight);
            facing = 1;
        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            //  esquerda corrida invertida
            objectSprite.translateX(-moveSpeed * delta);
            stateTime += delta;
            frameAtual = runAnimationLeft.getKeyFrame(stateTime, true);
            objectSprite.setRegion(frameAtual);
            objectSprite.setSize(originalWidth, originalHeight);
            facing = -1;
        } else {
            // parado dependendo da direção
            stateTime = 0f;
            if (facing >= 0) {
                objectSprite.setRegion(standingRegion);
            } else {
                objectSprite.setRegion(standingLeftRegion);
            }
            objectSprite.setSize(originalWidth, originalHeight);
        }

        // ---- Movimento teclado do pedal
        //   // pressione LEFT/RIGHT alternadamente para aumentar a velovidade do pedal
            // boolean leftJust = Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
            // boolean rightJust = Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);

            // if (leftJust || rightJust) {
            //     if (leftJust) {
            //         // duplo LEFT inverte a direção
            //         if (lastPedal == 1) {
            //             facing *= -1;
            //             lastPedal = 0;
            //         } else {
            //             pedalPower = Math.min(pedalMax, pedalPower + pedalInc);
            //             lastPedal = 1;
            //         }
            //     } else {
            //         if (lastPedal == 2) {
            //             // duplo RIGHT inverte a direção
            //             facing *= -1;
            //             lastPedal = 0;
            //         } else {
            //             pedalPower = Math.min(pedalMax, pedalPower + pedalInc);
            //             lastPedal = 2;
            //         }
            //     }
            // }

            // // aplica movimento com base no pedal
            // if (pedalPower > 0f) {
            //     // velocidade fixa quando o pedal tem alguma movimentação
            //     objectSprite.translateX(facing * moveSpeed * delta);
            //     stateTime += delta;
            //     frameAtual = (facing > 0) ? runAnimationRight.getKeyFrame(stateTime, true) : runAnimationLeft.getKeyFrame(stateTime, true);
            //     objectSprite.setRegion(frameAtual);
            //     objectSprite.setSize(originalWidth, originalHeight);
            // } else {
            //     // parado
            //     stateTime = 0f;
            //     objectSprite.setRegion(standingRegion);
            //     objectSprite.setSize(originalWidth, originalHeight);
            // }

            // // decaimento da potência do pedal
            // pedalPower = Math.max(0f, pedalPower - pedalDecay * delta);

            // Garante que o jogador não saia da tela
            objectSprite.setX(MathUtils.clamp(objectSprite.getX(), 0, worldWidth - playerWidth));
            objectSprite.setY(MathUtils.clamp(objectSprite.getY(), 0, worldHeight - playerHeight));
    }
    
}

