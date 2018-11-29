package com.spaceinvaders.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.spaceinvaders.game.logic.GameLogic
import com.spaceinvaders.game.SpaceInvaders
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable


class GameScreen(val game: SpaceInvaders): Screen {

    private var gameLogic: GameLogic
    private var stage: Stage
    private var seconds: Long

    companion object {

        lateinit var camera: OrthographicCamera
            private set
        lateinit var touchpad: Touchpad
        lateinit var shootBt: ImageButton

        const val WIDHT: Float = 800f
        const val HEIGHT: Float = 800f

        lateinit var assetManager: AssetManager
        lateinit var playerTexture: Texture
        lateinit var backgroundTexture: Texture
        lateinit var bluelaserTexture: Texture
        lateinit var redlaserTexture: Texture
        lateinit var squidTexture: Texture
        lateinit var crabTexture: Texture
        lateinit var crab2Texture: Texture
        lateinit var octopusTexture: Texture
        lateinit var octopus2Texture: Texture
        lateinit var octopus3Texture: Texture
        lateinit var ufoTexture: Texture
        lateinit var touchKnob: Texture
        lateinit var diedSound: Sound
        lateinit var killSound: Sound
        lateinit var shotSound: Sound
    }

    init {
        assetManager = AssetManager()
        assetManager.load("player.png", Texture::class.java)
        assetManager.load("space-2.png", Texture::class.java)
        assetManager.load("bluelaser.png", Texture::class.java)
        assetManager.load("redlaser.png", Texture::class.java)
        assetManager.load("squid.png", Texture::class.java)
        assetManager.load("crab.png", Texture::class.java)
        assetManager.load("crab2.png", Texture::class.java)
        assetManager.load("octopus.png", Texture::class.java)
        assetManager.load("octopus2.png", Texture::class.java)
        assetManager.load("octopus3.png", Texture::class.java)
        assetManager.load("ufo2.png", Texture::class.java)
        assetManager.load("touchKnob.png", Texture::class.java)
        assetManager.load("shot.mp3", Sound::class.java)
        assetManager.load("kill.wav", Sound::class.java)
        assetManager.load("dead.wav", Sound::class.java)
        assetManager.finishLoading()
        playerTexture = assetManager.get("player.png")
        backgroundTexture = assetManager.get("space-2.png")
        bluelaserTexture = assetManager.get("bluelaser.png")
        redlaserTexture = assetManager.get("redlaser.png")
        squidTexture = assetManager.get("squid.png")
        crabTexture = assetManager.get("crab.png")
        crab2Texture = assetManager.get("crab2.png")
        octopusTexture = assetManager.get("octopus.png")
        octopus2Texture = assetManager.get("octopus2.png")
        octopus3Texture = assetManager.get("octopus3.png")
        touchKnob = assetManager.get("touchKnob.png")
        ufoTexture = assetManager.get("ufo2.png")
        diedSound = assetManager.get("dead.wav")
        shotSound = assetManager.get("shot.mp3")
        killSound = assetManager.get("kill.wav")

        camera = OrthographicCamera()
        camera.setToOrtho(false, GameScreen.WIDHT, GameScreen.HEIGHT)

        stage = Stage()

        val padStyle : Touchpad.TouchpadStyle = Touchpad.TouchpadStyle()
        val touchpadSkin = Skin()
        touchpadSkin.add("touchKnob", touchKnob)
        val touchpadStyle = TouchpadStyle()
        val background = Pixmap(200, 200, Pixmap.Format.RGBA8888)
        background.blending = Pixmap.Blending.None
        background.setColor(1f, 1f, 1f, 0.1f)
        background.fillCircle(100, 100, 100)
        val touchKnobDraw = touchpadSkin.getDrawable("touchKnob")
        touchpadStyle.background = TextureRegionDrawable(TextureRegion(Texture(background)))
        touchpadStyle.knob = touchKnobDraw
        touchpad = Touchpad(10f, touchpadStyle)
        touchpad.setBounds(10f, 10f, 250f, 250f)

        shootBt = ImageButton(touchKnobDraw)
        shootBt.setBounds(520f, 10f, 200f, 200f)

        stage.addActor(touchpad)
        stage.addActor(shootBt)
        Gdx.input.setInputProcessor(stage)

        gameLogic = GameLogic()

        seconds = System.currentTimeMillis()
    }

    override fun render(delta: Float) {

        Gdx.gl.glClearColor(0f, 0f, 0.0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.setProjectionMatrix(camera.combined)
        game.batch.begin()
        game.batch.disableBlending()
        game.batch.draw(backgroundTexture, 0f, 0f, WIDHT, HEIGHT)

        gameLogic.update()

        if(gameLogic.lifes > 0) {
            game.batch.enableBlending()

            for (element in gameLogic.getAllElements())
                game.batch.draw(element.texture, element.body.x, element.body.y, element.body.width, element.body.height)

            game.font.data.setScale(0.5f, 0.5f)
            game.font.draw(game.batch, "Score: ${gameLogic.score}", 5f, HEIGHT - 5f)
            for (i in 1..gameLogic.lifes) {
                game.batch.draw(playerTexture, WIDHT - i * 40f, HEIGHT - 40f, 40f, 40f)
            }
            //game.font.draw(game.batch, " X:${touchpad.knobX} Y:${touchpad.knobY}", 5f, HEIGHT - 5f)
        }
        else{
            game.screen = EndScreen(game, gameLogic.score, (System.currentTimeMillis() - seconds)/1000)
        }
        game.font.data.setScale(1f, 1f)
        game.batch.end()

        stage.draw()
        stage.act()
    }

    override fun pause() {}
    override fun hide() {}
    override fun show() {}
    override fun resume() {}
    override fun resize(width: Int, height: Int) {}

    override fun dispose() {
        assetManager.dispose()
        game.screen = MenuScreen(game)
    }
}