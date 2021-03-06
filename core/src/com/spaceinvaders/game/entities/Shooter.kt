package com.spaceinvaders.game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture

interface ShooterInterface{
    fun shoot(x: Float, y: Float): Projectile
    fun canShoot(): Boolean
}

class Shooter(private val shootSpeed: Float, private val shootDelay: Float, private val shootTexture: Texture, timeInit: Float) : ShooterInterface{

    var timeSinceLastShoot = timeInit

    override fun shoot(x: Float, y: Float): Projectile {
        timeSinceLastShoot = 0f
        return Projectile(x, y, shootSpeed, shootTexture)
    }

    fun shoot(x: Float, y: Float, direction: Int): Projectile {
        timeSinceLastShoot = 0f
        return Projectile(x, y, shootSpeed, shootTexture, direction)
    }

    override fun canShoot(): Boolean{
        return timeSinceLastShoot > shootDelay
    }
}