package arcana.common.aspects;

import arcana.utils.Pair;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Aspects{
	public static BiMap<ResourceLocation, Aspect> ASPECTS = HashBiMap.create();
	
	public static final Aspect
			EMPTY = Aspect.create("empty", ColorRange.create(0x0, 0x0, 0x0, 0x0, 0x0)),
	
	AIR = Aspect.create("air", ColorRange.create(0x7c554b, 0xa7846a, 0xe8d2a0, 0xf4f0c4, 0xfdffe5)),
			CHAOS = Aspect.create("chaos", ColorRange.create(0x90909, 0x1e1e1f, 0x4d4d4d, 0x757377, 0x9a979c)),
			FIRE = Aspect.create("fire", ColorRange.create(0x972015, 0xd73d21, 0xed8a50, 0xf2c66c, 0xf4f2c6)),
			EARTH = Aspect.create("earth", ColorRange.create(0x1d6861, 0x2ba24e, 0x58dd5f, 0xb4f87e, 0xfdffe5)),
			ORDER = Aspect.create("order", ColorRange.create(0x6a686c, 0x9a979c, 0xb7b7b7, 0xd9d9d4, 0xfefff7)),
			WATER = Aspect.create("water", ColorRange.create(0x26317c, 0x4b6fb0, 0x4aaed6, 0xa0e2cb, 0xefeee6)),
	
	ARMOUR = Aspect.create("armour", ColorRange.create(0x4a456f, 0x685683, 0x7a71b4, 0xa589cb, 0xd0b5e0)),
			AURA = Aspect.create("aura", ColorRange.create(0x456978, 0x699fa7, 0x82c7bd, 0xa0e2cb, 0xefeee6)),
			BEAST = Aspect.create("beast", ColorRange.create(0x5a1925, 0x87332d, 0x9c533f, 0xb27357, 0xd09c6b)),
			CRAFTING = Aspect.create("crafting", ColorRange.create(0x381627, 0x5a2835, 0x804246, 0xa25f52, 0xb2826b)),
			CREATION = Aspect.create("creation", ColorRange.create(0x0, 0x0, 0x0, 0x0, 0x0)),
			CRYSTAL = Aspect.create("crystal", ColorRange.create(0x505fbe, 0x6aa5d6, 0x90cfed, 0xbeedf6, 0xffffff)),
			DARKNESS = Aspect.create("darkness", ColorRange.create(0x0, 0x0, 0x0, 0x0, 0x0)),
			DEATH = Aspect.create("death", ColorRange.create(0xb0b10, 0x1e1b28, 0x2c2438, 0x3f354a, 0x644d6a)),
			ELDRITCH = Aspect.create("eldritch", ColorRange.create(0x6a0627, 0xa70d29, 0xc71a14, 0xe65a2d, 0xf2af67)),
			ENDER = Aspect.create("ender", ColorRange.create(0x8080b, 0x1a1a26, 0x262036, 0x4d374b, 0x902d79)),
			ENERGY = Aspect.create("energy", ColorRange.create(0x411016, 0x681230, 0x971227, 0xb22053, 0xcf438f)),
			ENVY = Aspect.create("envy", ColorRange.create(0x160c20, 0x391848, 0x52215d, 0x6e2b71, 0x9c3394)),
			EXCHANGE = Aspect.create("exchange", ColorRange.create(0x78155d, 0xc22563, 0xe22d49, 0xeb9164, 0xf6cc8a)),
			FABRIC = Aspect.create("fabric", ColorRange.create(0x372961, 0x5b3781, 0x7e469e, 0xae59b7, 0xd284cd)),
			FLESH = Aspect.create("flesh", ColorRange.create(0x3b1531, 0x5a1e3f, 0x71263e, 0xa9404d, 0xbe6e5b)),
			FLIGHT = Aspect.create("flight", ColorRange.create(0x656d79, 0xa8b6bd, 0xbed1d4, 0xdee9e8, 0xffffff)),
			GLUTTONY = Aspect.create("gluttony", ColorRange.create(0x4b0a14, 0x811811, 0xb23a13, 0xd26821, 0xe9ad32)),
			GREED = Aspect.create("greed", ColorRange.create(0xa94f0d, 0xd08713, 0xefcc25, 0xf4f62d, 0xfdffe2)),
			HARVEST = Aspect.create("harvest", ColorRange.create(0x256d6f, 0x35937b, 0x46b489, 0x54dd78, 0x8af17f)),
			HUMAN = Aspect.create("human", ColorRange.create(0x160a0e, 0x261215, 0x3b211a, 0x513928, 0x6a4f33)),
			ICE = Aspect.create("ice", ColorRange.create(0x678ab4, 0x94c1e0, 0xadddeb, 0xd3f6fb, 0xfafbfb)),
			IMPRISON = Aspect.create("imprison", ColorRange.create(0x131214, 0x292629, 0x51464c, 0x756168, 0x907b7e)),
			JOURNEY = Aspect.create("journey", ColorRange.create(0x8a2723, 0xb46147, 0xc69876, 0xd6b68a, 0xe8d2a0)),
			LIFE = Aspect.create("life", ColorRange.create(0x712269, 0x883462, 0xb21f4e, 0xd2243a, 0xe2422a)),
			LIGHT = Aspect.create("light", ColorRange.create(0xd6ae9e, 0xedd6c0, 0xfdf1dd, 0xffffff, 0xffffff)),
			LUST = Aspect.create("lust", ColorRange.create(0x460742, 0x780b55, 0xd00970, 0xfb1772, 0xfba2b2)),
			MACHINE = Aspect.create("machine", ColorRange.create(0xd0d1b, 0x201a34, 0x2a2b56, 0x404e6f, 0x52677e)),
			MANA = Aspect.create("mana", ColorRange.create(0x400e6f, 0x8d1ca9, 0xc21ec7, 0xe242b3, 0xf881d6)),
			METAL = Aspect.create("metal", ColorRange.create(0x3a383b, 0x504d51, 0x6e6a6f, 0x97939a, 0xb7b5c2)),
			MINING = Aspect.create("mining", ColorRange.create(0xae6658, 0xd09367, 0xe9bf7c, 0xfaeaa8, 0xfafbd4)),
			MIND = Aspect.create("mind", ColorRange.create(0x116d95, 0x14cbcb, 0x33e9ce, 0x74fab8, 0xd3fbd2)),
			MOVEMENT = Aspect.create("movement", ColorRange.create(0x716468, 0x918a8a, 0xab9a96, 0xd0b9ac, 0xe6d3c0)),
			NETHER = Aspect.create("nether", ColorRange.create(0x141229, 0x251b42, 0x481d5c, 0x631d6f, 0x911f8d)),
			OVERWORLD = Aspect.create("overworld", ColorRange.create(0x261e1d, 0x3d312c, 0x544c3e, 0x677841, 0x6dac4c)),
			PLANT = Aspect.create("plant", ColorRange.create(0x30a01d, 0x95e92e, 0xcaf464, 0xeef88d, 0xf7f8e6)),
			PRIDE = Aspect.create("pride", ColorRange.create(0x227748, 0x2da23a, 0x88ed6a, 0xc1fb99, 0xf4fad5)),
			SEEDS = Aspect.create("seeds", ColorRange.create(0x4f5c22, 0x7a7927, 0x8d9135, 0xab983d, 0xc2bd59)),
			SENSES = Aspect.create("senses", ColorRange.create(0x38258e, 0x3f4dbb, 0x5374cf, 0x73b0f2, 0xa2dff6)),
			SLIME = Aspect.create("slime", ColorRange.create(0x438844, 0x5eb06b, 0x79d473, 0xc9f181, 0xfbffe5)),
			SLOTH = Aspect.create("sloth", ColorRange.create(0xc365d, 0xd5f7e, 0x2699a9, 0x3dcbcb, 0x5cede0)),
			SPIRIT = Aspect.create("spirit", ColorRange.create(0x56638c, 0x688aa3, 0x8dc4cf, 0xc1f4ea, 0xf3ffff)),
			STRENGTH = Aspect.create("strength", ColorRange.create(0x251c53, 0x2f3480, 0x2f5d95, 0x447ea7, 0x5aa8bb)),
			TAINT = Aspect.create("taint", ColorRange.create(0x231638, 0x4e2c5c, 0x6e3877, 0x903e8e, 0xa95aa0)),
			TOOL = Aspect.create("tool", ColorRange.create(0x27223d, 0x373556, 0x41487c, 0x4c6497, 0x608eb4)),
			TREE = Aspect.create("tree", ColorRange.create(0x462635, 0x5d303b, 0x783b41, 0x955549, 0xb06756)),
			UNDEAD = Aspect.create("undead", ColorRange.create(0x16261e, 0x1f4d27, 0x396c28, 0x6d8e37, 0xb9c450)),
			VACUUM = Aspect.create("vacuum", ColorRange.create(0x1a1a26, 0x1a1a26, 0x1a1a26, 0x262036, 0x2e2c4b)),
			VENOM = Aspect.create("venom", ColorRange.create(0xa0812, 0x1d1429, 0x2c223f, 0x2f3953, 0x36685d)),
			WEAPON = Aspect.create("weapon", ColorRange.create(0x6a1445, 0x911b41, 0xb71831, 0xd94023, 0xf67c30)),
			WIND = Aspect.create("wind", ColorRange.create(0x5d5f71, 0x888599, 0x9ea0bd, 0xccd0df, 0xfffde8)),
			WRATH = Aspect.create("wrath", ColorRange.create(0x5a0b3d, 0x8e0f3e, 0xc61626, 0xed1507, 0xffbe8d));

	//do not change order!
	public static final Aspect[] primal = new Aspect[]{Aspects.AIR, Aspects.WATER, Aspects.EARTH, Aspects.FIRE, Aspects.ORDER, Aspects.CHAOS};
	public static final Aspect[] sin = new Aspect[]{Aspects.ENVY, Aspects.LUST, Aspects.SLOTH, Aspects.PRIDE, Aspects.GREED, Aspects.WRATH, Aspects.GLUTTONY};

	public static final BiMap<Pair<Aspect, Aspect>, Aspect> COMBINATIONS = HashBiMap.create(Aspects.getList().size() - 1 - primal.length);
	static{
		COMBINATIONS.put(Pair.of(FIRE, CHAOS), ICE);
		COMBINATIONS.put(Pair.of(FIRE, AIR), LIGHT);
		COMBINATIONS.put(Pair.of(ORDER, AIR), MOVEMENT);
		COMBINATIONS.put(Pair.of(ORDER, CHAOS), EXCHANGE);
		COMBINATIONS.put(Pair.of(FIRE, ORDER), STRENGTH);
		COMBINATIONS.put(Pair.of(AIR, WATER), WIND);
		COMBINATIONS.put(Pair.of(AIR, CHAOS), VACUUM);
		COMBINATIONS.put(Pair.of(WATER, CHAOS), VENOM);
		COMBINATIONS.put(Pair.of(WATER, EARTH), LIFE);
		COMBINATIONS.put(Pair.of(ORDER, EARTH), CRYSTAL);
		
		COMBINATIONS.put(Pair.of(LIGHT, LIFE), OVERWORLD);
		COMBINATIONS.put(Pair.of(MOVEMENT, LIFE), BEAST);
		COMBINATIONS.put(Pair.of(EARTH, LIFE), PLANT);
		COMBINATIONS.put(Pair.of(EARTH, MOVEMENT), JOURNEY);
		COMBINATIONS.put(Pair.of(WATER, LIFE), SLIME);
		COMBINATIONS.put(Pair.of(EARTH, CRYSTAL), METAL);
		COMBINATIONS.put(Pair.of(LIFE, CHAOS), DEATH);
		COMBINATIONS.put(Pair.of(STRENGTH, VACUUM), MANA);
		COMBINATIONS.put(Pair.of(LIGHT, VACUUM), DARKNESS);
		COMBINATIONS.put(Pair.of(MOVEMENT, CHAOS), IMPRISON);
		COMBINATIONS.put(Pair.of(AIR, MOVEMENT), FLIGHT);
		
		COMBINATIONS.put(Pair.of(VACUUM, DARKNESS), ENDER);
		COMBINATIONS.put(Pair.of(ORDER, FIRE), ENERGY);
		COMBINATIONS.put(Pair.of(AIR, PLANT), TREE);
		COMBINATIONS.put(Pair.of(AIR, MANA), AURA);
		COMBINATIONS.put(Pair.of(BEAST, DEATH), FLESH);
		COMBINATIONS.put(Pair.of(MOVEMENT, DEATH), UNDEAD);
		COMBINATIONS.put(Pair.of(LIFE, DEATH), SPIRIT);
		
		COMBINATIONS.put(Pair.of(AIR, SPIRIT), SENSES);
		COMBINATIONS.put(Pair.of(ORDER, SPIRIT), MIND);
		COMBINATIONS.put(Pair.of(FIRE, SPIRIT), NETHER);
		
		COMBINATIONS.put(Pair.of(MIND, CHAOS), ELDRITCH);
		COMBINATIONS.put(Pair.of(MANA, CHAOS), TAINT);
		COMBINATIONS.put(Pair.of(MIND, SENSES), CREATION);
		
		COMBINATIONS.put(Pair.of(CREATION, ORDER), TOOL);
		COMBINATIONS.put(Pair.of(CREATION, PLANT), SEEDS);
		COMBINATIONS.put(Pair.of(CREATION, EARTH), MINING);
		
		COMBINATIONS.put(Pair.of(CREATION, TOOL), CRAFTING);
		COMBINATIONS.put(Pair.of(MOVEMENT, TOOL), MACHINE);
		COMBINATIONS.put(Pair.of(SEEDS, TOOL), HARVEST);
		COMBINATIONS.put(Pair.of(BEAST, TOOL), FABRIC);
		COMBINATIONS.put(Pair.of(FIRE, TOOL), WEAPON);
		COMBINATIONS.put(Pair.of(CREATION, TOOL), ARMOUR);
		COMBINATIONS.put(Pair.of(CREATION, MIND), HUMAN);
		
		COMBINATIONS.put(Pair.of(HUMAN, FLESH), LUST);
		COMBINATIONS.put(Pair.of(HUMAN, IMPRISON), SLOTH);
		COMBINATIONS.put(Pair.of(HUMAN, VACUUM), GLUTTONY);
		COMBINATIONS.put(Pair.of(HUMAN, WEAPON), WRATH);
		COMBINATIONS.put(Pair.of(HUMAN, CREATION), ENVY);
		COMBINATIONS.put(Pair.of(HUMAN, FABRIC), PRIDE);
		COMBINATIONS.put(Pair.of(HUMAN, METAL), GREED);
	}
	
	public static final List<Pair<Aspect, Aspect>> COMBOS_AS_LIST = new ArrayList<>(COMBINATIONS.keySet());

	public static Aspect getCompound(Pair<Aspect, Aspect> components){
		return Aspects.COMBINATIONS.containsKey(components) ? Aspects.COMBINATIONS.get(components) :
			Aspects.COMBINATIONS.getOrDefault(components.flip(), EMPTY);
	}
	
	/**
	 * Returns list of ALL aspects. Including Empty.
	 *
	 * @return ALL aspects.
	 */
	public static List<Aspect> getList(){
		return new ArrayList<>(ASPECTS.values());
	}

	public static Set<Aspect> getSet(){
		return ASPECTS.values();
	}

	public static Aspect get(ResourceLocation id){
		return ASPECTS.get(id);
	}
}