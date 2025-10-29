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
    private int lastPedal = 0; // 1 = LEFT, 2 = RIGHT
    private int facing = 1; // 1 = right, -1 = left (starts to the right)
    private float pedalPower = 0f;
    private final float pedalInc = 2f;
    private final float pedalMax = 6f;
    private final float pedalDecay = 3f;
    // velocidade fixa do jogador (unidades do mundo por segundo)
    private final float moveSpeed = 2f;

    public Player(Texture texture, float x, float y, float width, float height, Viewport viewport) {
        super(texture, x, y, width, height);
        this.viewport = viewport;
        this.touchPos = new Vector2();
        
        // armazena o tamanho original da sprite 
        this.originalWidth = objectSprite.getWidth();
        this.originalHeight = objectSprite.getHeight();

        // quando o personagem está parado
        this.standingRegion = new TextureRegion(texture);

        // frames de corrida (direita) 
        runRightTextures = new Texture[6];
        TextureRegion[] rightRegions = new TextureRegion[6];
        for (int i = 0; i < 6; i++){
            runRightTextures[i] = new Texture("Run" + (i+1) + ".png");
            rightRegions[i] = new TextureRegion(runRightTextures[i]);
        }
        runAnimationRight = new Animation<>(0.1f, rightRegions);

        // animação da esquerda espelhando as da direita
        TextureRegion[] leftRegions = new TextureRegion[6];
        for (int i = 0; i < 6; i++){
            leftRegions[i] = new TextureRegion(rightRegions[i]);
            leftRegions[i].flip(true, false);
        }
        runAnimationLeft = new Animation<>(0.1f, leftRegions);
    }

    @Override
    public void update(float delta) {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float playerWidth = objectSprite.getWidth();
        float playerHeight = objectSprite.getHeight();

        // Movimento teclado do pedal
            // pressione LEFT/RIGHT alternadamente para aumentar a potência do pedal
            boolean leftJust = Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
            boolean rightJust = Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);

            if (leftJust || rightJust) {
                if (leftJust) {
                    // duplo LEFT inverte a direção
                    if (lastPedal == 1) {
                        facing *= -1;
                        lastPedal = 0;
                    } else {
                        pedalPower = Math.min(pedalMax, pedalPower + pedalInc);
                        lastPedal = 1;
                    }
                } else {
                    if (lastPedal == 2) {
                        // duplo RIGHT inverte a direção
                        facing *= -1;
                        lastPedal = 0;
                    } else {
                        pedalPower = Math.min(pedalMax, pedalPower + pedalInc);
                        lastPedal = 2;
                    }
                }
            }

            // aplica movimento com base na potência do pedal
            if (pedalPower > 0f) {
                // movimenta com velocidade fixa quando o pedal tem alguma potência
                objectSprite.translateX(facing * moveSpeed * delta);
                stateTime += delta;
                frameAtual = (facing > 0) ? runAnimationRight.getKeyFrame(stateTime, true) : runAnimationLeft.getKeyFrame(stateTime, true);
                objectSprite.setRegion(frameAtual);
                objectSprite.setSize(originalWidth, originalHeight);
            } else {
                // parado
                stateTime = 0f;
                objectSprite.setRegion(standingRegion);
                objectSprite.setSize(originalWidth, originalHeight);
            }

            // decaimento da potência do pedal
            pedalPower = Math.max(0f, pedalPower - pedalDecay * delta);

            // // Movimento via toque ou mouse
            // if (Gdx.input.isTouched()) {
            //     touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            //     viewport.unproject(touchPos); // conversao da unidadae da viewport para o vetor entender
            //     objectSprite.setCenterX(touchPos.x);
            //     objectSprite.setCenterY(touchPos.y);
            // }

            // Garante que o jogador não saia da tela
            objectSprite.setX(MathUtils.clamp(objectSprite.getX(), 0, worldWidth - playerWidth));
            objectSprite.setY(MathUtils.clamp(objectSprite.getY(), 0, worldHeight - playerHeight));
    }
    
}

