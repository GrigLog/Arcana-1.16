package arcana.server.worldgen

import arcana.common.aspects.Aspects

object ModFeatures {
    val towerAir = Tower(Aspects.AIR)
    val towerWater = Tower(Aspects.WATER)
    val towerEarth = Tower(Aspects.EARTH)
    val towerFire = Tower(Aspects.FIRE)
    val towerOrder = Tower(Aspects.ORDER)
    val towerChaos = Tower(Aspects.CHAOS)
}