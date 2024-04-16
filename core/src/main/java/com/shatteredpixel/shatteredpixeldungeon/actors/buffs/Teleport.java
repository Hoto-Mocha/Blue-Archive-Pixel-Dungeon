package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Teleport extends Buff implements ActionIndicator.Action {

    {
        revivePersists = true;
    }

    boolean canUse;

    private static final String CANUSE = "canUse";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CANUSE, canUse);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        canUse = bundle.getBoolean(CANUSE);
        if (canUse) {
            ActionIndicator.setAction(this);
        }
    }

    @Override
    public void detach() { //일반적으로 이 버프는 해제될 일이 없음
        super.detach();
        canUse = false;
        ActionIndicator.clearAction(this);
    }

    @Override
    public boolean act() {
        if (hero.buff(TeleportCooldown.class) == null) {
            canUse = true;
            ActionIndicator.setAction(this);
            BuffIndicator.refreshHero();
        }
        spend(TICK);

        return true;
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.TELEPORT;
    }

    @Override
    public int indicatorColor() {
        return 0xFDA082;
    }

    @Override
    public void doAction() {
        GameScene.selectCell( selector );
    }

    private CellSelector.Listener selector = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (target != hero.pos && hero.rooted){
                    hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_teleport_rooted")); //"앗! 발이 묶여 있어요!"
                    PixelScene.shake( 1, 1f );
                    return;
                }

                PathFinder.buildDistanceMap(hero.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null), 6);

                if ( PathFinder.distance[target] == Integer.MAX_VALUE ||
                        !Dungeon.level.heroFOV[target] ||
                        (target != hero.pos && Actor.findChar( target ) != null)) {

                    hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_teleport_wrong_pos")); //"주군, 그 곳으로는 이동할 수 없어요!"
                    return;
                }

                if (hero.hasTalent(Talent.TELEPORT_1)){
                    //effectively 1/2/3/4 turns
                    float duration = 0.67f + hero.pointsInTalent(Talent.TELEPORT_1);
                    Buff.affect(hero, Haste.class, duration);
                    Buff.affect(hero, Invisibility.class, duration);
                }

                if (hero.hasTalent(Talent.TELEPORT_2)) {
                    for (Char ch : Actor.chars()){
                        if (ch instanceof NinjaLog){
                            ch.die(null);
                        }
                    }

                    NinjaLog n = new NinjaLog();
                    n.pos = hero.pos;
                    GameScene.add(n);
                    Dungeon.level.occupyCell(n);
                }

                if (hero.hasTalent(Talent.TELEPORT_3)) {
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (Dungeon.level.adjacent(mob.pos, hero.pos) && mob.alignment != Char.Alignment.ALLY) {
                            Buff.prolong(mob, Blindness.class, 3*hero.pointsInTalent(Talent.TELEPORT_3));
                            if (mob.state == mob.HUNTING) mob.state = mob.WANDERING;
                            mob.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 4);
                        }
                    }
                }

                CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 10 );
                ScrollOfTeleportation.appear( hero, target );
                Sample.INSTANCE.play( Assets.Sounds.PUFF );
                Dungeon.level.occupyCell( hero );
                Dungeon.observe();
                GameScene.updateFog();

                hero.yellI(Messages.get(Hero.class, hero.heroClass.name() + "_teleport_use_" + (1+ Random.Int(3)))); //"이즈나 비기! 바꿔치기!", "샤샤삭!", "적을 교란하겠습니다!"
                hero.spendAndNext(Actor.TICK);

                canUse = false;
                ActionIndicator.clearAction(Teleport.this);
                Buff.affect(hero, TeleportCooldown.class);
            }
        }

        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

    public static class TeleportCooldown extends Buff {

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xFDA082);
        }

        public void kill() {
            detach();
            BuffIndicator.refreshHero();
            if (hero.buff(Teleport.class) != null) {
                ActionIndicator.setAction(hero.buff(Teleport.class));
            }
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }
    }

    public static class NinjaLog extends NPC {

        {
            spriteClass = NinjaLogSprite.class;
            defenseSkill = 0;

            properties.add(Property.INORGANIC); //wood is organic, but this is accurate for game logic

            alignment = Alignment.ALLY;

            HP = HT = 20*Dungeon.hero.pointsInTalent(Talent.TELEPORT_2);
        }

        @Override
        public int drRoll() {
            int dr = super.drRoll();

            dr += Random.NormalIntRange(Dungeon.hero.pointsInTalent(Talent.TELEPORT_2),
                    3*Dungeon.hero.pointsInTalent(Talent.TELEPORT_2));

            return dr;
        }

        {
            immunities.add( Dread.class );
            immunities.add( Terror.class );
            immunities.add( Amok.class );
            immunities.add( Charm.class );
            immunities.add( Sleep.class );
            immunities.add( AllyBuff.class );
        }

    }

    public static class NinjaLogSprite extends MobSprite {

        public NinjaLogSprite(){
            super();

            texture( Assets.Sprites.NINJA_LOG );

            TextureFilm frames = new TextureFilm( texture, 11, 12 );

            idle = new Animation( 0, true );
            idle.frames( frames, 0 );

            run = idle.clone();
            attack = idle.clone();
            zap = attack.clone();

            die = new Animation( 12, false );
            die.frames( frames, 1, 2, 3, 4 );

            play( idle );

        }

        @Override
        public void showAlert() {
            //do nothing
        }

        @Override
        public int blood() {
            return 0xFF966400;
        }

    }
}
