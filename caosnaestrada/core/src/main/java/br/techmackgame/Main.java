// package br.techmackgame;

// import com.badlogic.gdx.ApplicationListener;
// import com.badlogic.gdx.Gdx;
// import com.badlogic.gdx.graphics.Color;
// import com.badlogic.gdx.graphics.Texture;
// import com.badlogic.gdx.graphics.g2d.SpriteBatch;
// import com.badlogic.gdx.math.MathUtils;
// import com.badlogic.gdx.utils.Array;
// import com.badlogic.gdx.utils.ScreenUtils;
// import com.badlogic.gdx.utils.viewport.FitViewport;

// public class Main implements ApplicationListener {

//     SpriteBatch spriteBatch;
//     FitViewport viewport;

//     Player player;
//     Truck truck;

//     Array<Texture> objectTextures;
//     FallingObject fallingObject;
//     float spawnTimer = 0f;
//     float spawnInterval = 3f;

//     // Fundo
//     Texture backgroundTexture;
//     float bgX1, bgX2;
//     float bgSpeed = 2f; // velocidade de movimento do fundo
//     float bgWidthUnits;

//     // Menu
//     Menu menu;
//     boolean gameStarted = false;

//     @Override
//     public void create() {
//         spriteBatch = new SpriteBatch();
//         viewport = new FitViewport(8, 5);

//         menu = new Menu();

//         // Player
//         Texture playerTexture = new Texture("standingRight.png");
//         player = new Player(playerTexture, 1, 1, 0.5f, 1f, viewport);

//         // Caminhão
//         float truckWidth = 4f;
//         float truckHeight = 2f;
//         float truckX = viewport.getWorldWidth() - truckWidth / 2;
//         float truckY = 1f;
//         truck = new Truck(truckX, truckY, truckWidth, truckHeight);

//         // Objetos aleatórios (cada um é uma imagem separada)
//         objectTextures = new Array<>();
//         objectTextures.add(new Texture("abajur.png"));
//         objectTextures.add(new Texture("brinquedos.png"));
//         objectTextures.add(new Texture("notebook.png"));
//         objectTextures.add(new Texture("roupas.png"));
//         objectTextures.add(new Texture("travesseiro.png"));

//         // Fundo
//         backgroundTexture = new Texture("backgroundlvl1.png");
//         bgWidthUnits = 8f; // largura do fundo em unidades do mundo (ajuste se necessário)
//         bgX1 = 0;
//         bgX2 = bgWidthUnits;
//     }

//     @Override
//     public void resize(int width, int height) {
//         viewport.update(width, height, true);
//     }

//     @Override
//     public void render() {
//         float delta = Gdx.graphics.getDeltaTime();
//         if (!gameStarted) {
//                     // Atualiza menu
//                     menu.update();

//                     ScreenUtils.clear(Color.BLACK);
//                     viewport.apply();
//                     spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
//                     spriteBatch.begin();
//                     menu.render();
//                     spriteBatch.end();

//                     // Verifica clique em jogar ou sair
//                     if (menu.shouldStartGame()) gameStarted = true;
//                     if (menu.shouldExitGame()) Gdx.app.exit();
//                 } else {
//                     // Jogo
//                     updateGameObjects(delta);
//                     drawGameObjects();
//                 }
//     }

//     private void updateGameObjects(float delta) {
//         player.update(delta);
//         truck.update(delta);

//         // Impede o player de entrar no caminhão
//         if (player.getBounds().overlaps(truck.getBounds())) {
//             if (player.getX() < truck.getX()) {
//                 player.setPosition(truck.getX() - player.getWidth(), player.getY());
//             } else {
//                 player.setPosition(truck.getX() + truck.getWidth(), player.getY());
//             }
//         }

//         // Controle de spawn de objetos
//         spawnTimer += delta;
//         if ((fallingObject == null || !fallingObject.isActive()) && spawnTimer > spawnInterval) {
//             spawnTimer = 0f;
//             spawnInterval = MathUtils.random(2f, 5f);

//             Texture randomTexture = objectTextures.random();
//             float startX = truck.getX(); // sai da traseira do caminhão
//             float startY = truck.getY() + truck.getHeight();

//             fallingObject = new FallingObject(randomTexture, startX, startY, 0.5f, 0.5f);
//         }

//         // Atualiza e verifica colisão com o player
//         if (fallingObject != null) {
//             fallingObject.update(delta);

//             if (fallingObject.isActive() && player.getBounds().overlaps(fallingObject.getBounds())) {
//                 fallingObject.collect();
//                 System.out.println("🎯 Player pegou o objeto!");
//             }
//         }

//         //Fundo
        
//         // Move o fundo
//         bgX1 -= bgSpeed * delta;
//         bgX2 -= bgSpeed * delta;

//         // Reposiciona quando sair da tela
//         if (bgX1 + bgWidthUnits < 0) {
//             bgX1 = bgX2 + bgWidthUnits;
//         }
//         if (bgX2 + bgWidthUnits < 0) {
//             bgX2 = bgX1 + bgWidthUnits;
//         }
//     }

//     private void drawGameObjects() {
//         ScreenUtils.clear(Color.BLACK);
//         viewport.apply();
//         spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

//         spriteBatch.begin();

//         // Fundo: desenha primeira cópia normal
//         spriteBatch.draw(backgroundTexture, bgX1, 0, bgWidthUnits, viewport.getWorldHeight());

//         // Fundo: desenha segunda cópia invertida horizontalmente
//         spriteBatch.draw(backgroundTexture, bgX2 + bgWidthUnits, 0, -bgWidthUnits, viewport.getWorldHeight());

//         // desenha player
//         player.draw(spriteBatch);

//         // desenha caminhão
//         truck.draw(spriteBatch);
//         if (fallingObject != null) fallingObject.draw(spriteBatch);

//         spriteBatch.end();
//     }

//     @Override public void pause() { }
//     @Override public void resume() { }

//     @Override
//     public void dispose() {
//         spriteBatch.dispose();
//         for (Texture t : objectTextures) t.dispose();
//         backgroundTexture.dispose();
//         menu.dispose();
//     }
// }

package br.techmackgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {

    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Menu menu;
    private boolean gameStarted = false;
    private Level currentLevel;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        menu = new Menu();

        // 🔹 começa mostrando o primeiro nível no carrossel (índice 0 = Level 1)
        menu.setSelectedLevel(0);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (!gameStarted) {
            // atualiza e desenha o menu
            menu.update();
            ScreenUtils.clear(Color.BLACK);
            menu.render();

            if (menu.shouldStartGame()) {
                int selected = menu.getSelectedLevel(); // agora 0 = Level1, 1 = Level2, etc.

                switch (selected) {
                    case 0:
                        currentLevel = new Level1(viewport, spriteBatch);
                        break;
                    case 1:
                        currentLevel = new Level2(viewport, spriteBatch);
                        break;
                    case 2:
                        currentLevel = new Level3(viewport, spriteBatch);
                        break;
                    default:
                        currentLevel = new Level1(viewport, spriteBatch);
                        break;
                }

                System.out.println("🚀 Iniciando Level " + (selected + 1));
                gameStarted = true;
                menu.dispose();
            }

            if (menu.shouldExitGame()) {
                Gdx.app.exit();
            }

        } else {
            // renderiza o nível atual
            if (currentLevel != null) {
                currentLevel.update(delta);
                currentLevel.render();

                // se o nível terminou, vai pro próximo
                if (currentLevel.isLevelComplete()) {
                    int next = menu.getSelectedLevel() + 1;
                    if (next >= 3) next = 0; // volta pro primeiro após o último
                    menu.setSelectedLevel(next);

                    System.out.println("Indo para Level " + (next + 1));

                    // troca pro novo nível
                    currentLevel.dispose();
                    switch (next) {
                        case 0:
                            currentLevel = new Level1(viewport, spriteBatch);
                            break;
                        case 1:
                            currentLevel = new Level2(viewport, spriteBatch);
                            break;
                         case 2:
                            currentLevel = new Level3(viewport, spriteBatch);
                            break;
                        default:
                            currentLevel = new Level1(viewport, spriteBatch);
                            break;
                    }
                }

            } else {
                // segurança
                System.err.println("currentLevel está nulo, retornando ao menu...");
                gameStarted = false;
                menu = new Menu();
                menu.setSelectedLevel(0);
            }
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        if (spriteBatch != null) spriteBatch.dispose();
        if (menu != null) menu.dispose();
        if (currentLevel != null) currentLevel.dispose();
    }
}