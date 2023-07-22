package com.shatteredpixel.shatteredpixeldungeon.items;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SuperNova extends Item {
    {
        image = ItemSpriteSheet.SUPER_NOVA;

        defaultAction = AC_SHOOT;
        usesTargeting = false;

        bones = false;
        unique = true;
    }

    private static final String AC_SHOOT = "SHOOT";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_SHOOT );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals(AC_SHOOT)) {
            if (hero.buff(SuperNovaCooldown.class) != null) {
                hero.yellW(Messages.get(Hero.class, "aris_supernova_cooldown"));
            } else {
                usesTargeting = true;
                curUser = hero;
                curItem = this;
                GameScene.selectCell(shooter);
            }
        }
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (target == curUser.pos) {
                    hero.yellP(Messages.get(Hero.class, "aris_cannot_self"));
                } else {
                    boolean terrainAffected = false;
                    int maxDistance = 5+2*hero.lvl;
                    Ballistica beam = new Ballistica(curUser.pos, target, Ballistica.WONT_STOP);
                    ArrayList<Char> chars = new ArrayList<>();
                    for (int c : beam.subPath(1, maxDistance)) {
                        Char ch;
                        if ((ch = Actor.findChar( c )) != null) {
                            if (ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).PASSIVE
                                    && !(Dungeon.level.mapped[c] || Dungeon.level.visited[c])){
                                //avoid harming undiscovered passive chars
                            } else {
                                chars.add(ch);
                            }
                        }
                        if (Dungeon.level.flamable[c]) {
                            Dungeon.level.destroy( c );
                            GameScene.updateMap( c );
                            terrainAffected = true;
                        }
                        CellEmitter.center( c ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
                        if (terrainAffected) {
                            Dungeon.observe();
                        }
                    }
                    for (Char ch : chars) {
                        ch.damage( Random.NormalIntRange(ch.HT/2, hero.lvl*5+Dungeon.depth*2), this );
                        ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
                        ch.sprite.flash();
                    }

                    curUser.sprite.zap(target);
                    int cell = beam.path.get(Math.min(beam.dist, maxDistance));
                    curUser.sprite.parent.add(new Beam.DeathRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( cell )));
                    Dungeon.hero.yellP(Messages.get(Hero.class, "aris_supernova_" + Integer.toString(Random.Int(3)+1)));
                }
                Buff.affect(hero, SuperNovaCooldown.class).set(50);
                hero.spendAndNext(Actor.TICK);
            }
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

    public static class SuperNovaCooldown extends Buff {

        float maxDuration;
        float duration;

        {
            type = buffType.NEUTRAL;
        }

        public void set(int time) {
            maxDuration = time;
            duration = maxDuration;
        }

        @Override
        public boolean act() {
            duration-=TICK;
            spend(TICK);
            if (duration <= 0) {
                detach();
            }
            BuffIndicator.refreshHero();
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (maxDuration - duration) / maxDuration);
        }

        private static final String DURATION  = "duration";
        private static final String MAX_DURATION  = "maxDuration";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DURATION, duration);
            bundle.put(MAX_DURATION, maxDuration);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            duration = bundle.getFloat( DURATION );
            maxDuration = bundle.getFloat( MAX_DURATION );
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", Messages.decimalFormat("#.##", duration));
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return -1;
    }
}
