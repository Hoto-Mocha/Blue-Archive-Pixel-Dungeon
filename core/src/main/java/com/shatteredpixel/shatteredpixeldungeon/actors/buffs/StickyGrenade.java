package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.GL;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
public class StickyGrenade extends Buff {

    {
        type = buffType.NEUTRAL;
        actPriority = MOB_PRIO - 1;
    }

    private int timer = 3;
    private int stuck = 0;
    private int minDmg = 0;
    private int maxDmg = 0;

    public void attach(Gun gunUsed) {
        this.minDmg = gunUsed.bulletMin();
        this.maxDmg = gunUsed.bulletMax();
        stuck++;
    }

    @Override
    public int icon() {
        return BuffIndicator.BULLET;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0xFB8288);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (3 - timer) / 3);
    }

    @Override
    public boolean act() {
        int pos = target.pos;
        PointF p = DungeonTilemap.raisedTileCenterToWorld(pos);
        if (timer == 3) {
            if (Dungeon.level.heroFOV[pos]) FloatingText.show(p.x, p.y, pos, "3...", CharSprite.NEUTRAL);
        } else if (timer == 2){
            if (Dungeon.level.heroFOV[pos]) FloatingText.show(p.x, p.y, pos, "2...", CharSprite.WARNING);
        } else if (timer == 1){
            if (Dungeon.level.heroFOV[pos]) FloatingText.show(p.x, p.y, pos, "1...", CharSprite.NEGATIVE);
        } else {
            detach();
            return true;
        }

        timer--;
        spend(TICK);
        return true;
    }

    @Override
    public void detach() {
        super.detach();
        int pos = target.pos;
        if (stuck > 0) {
            float dmgMulti = 1 + (stuck-1) * 0.05f * hero.pointsInTalent(Talent.STICKY_GRENADE_1);
            float accMulti = 1 + hero.pointsInTalent(Talent.STICKY_GRENADE_3);
            for (int c : PathFinder.NEIGHBOURS9) {
                int cell = pos + c;
                if (cell >= 0 && cell < Dungeon.level.length()) {
                    Char ch = Actor.findChar(cell);
                    if (ch != null) {
                        for (int i = 0; i < stuck; i++) {
                            int damage = Math.round(Random.NormalIntRange(minDmg, maxDmg) * dmgMulti);
                            hero.shoot(ch, new Sticker(), 1, damage, accMulti);

                            if (ch == hero){
                                if (!ch.isAlive()) {
                                    Dungeon.fail(Gun.Bullet.class);
                                }
                            }
                        }
                    }

                    if (Dungeon.level.heroFOV[cell]) {
                        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);
                        CellEmitter.center(pos).burst(BlastParticle.FACTORY, 4);
                    }
                    if (Dungeon.level.flamable[cell]) {
                        Dungeon.level.destroy(cell);
                        GameScene.updateMap(cell);
                    }
                }
            }

            Sample.INSTANCE.play(Assets.Sounds.BLAST);
        }
    }

    private static final String TIMER         = "timer";
    private static final String STUCK         = "stuck";
    private static final String MIN_DMG       = "minDmg";
    private static final String MAX_DMG       = "maxDmg";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );

        bundle.put( TIMER, timer );
        bundle.put( STUCK, stuck );
        bundle.put( MIN_DMG, minDmg );
        bundle.put( MAX_DMG, maxDmg );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        timer = bundle.getInt( TIMER );
        stuck = bundle.getInt( STUCK );
        minDmg = bundle.getInt( MIN_DMG );
        maxDmg = bundle.getInt( MAX_DMG );

    }

    public static class Sticker extends MissileWeapon {
        {
            hitSound = Assets.Sounds.PUFF;
        }
    }
}
