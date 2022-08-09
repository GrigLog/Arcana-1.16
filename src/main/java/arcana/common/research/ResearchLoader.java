package arcana.common.research;

import arcana.Arcana;
import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class ResearchLoader extends JsonReloadListener {
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	
	public ResearchLoader(){
		super(GSON, "research");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> files, IResourceManager resourceManager, IProfiler profiler){
		ServerResearchManager.requirements.clear();
		ServerResearchManager.files = files;
		Requirement.initParser();
		files.forEach((location, file) -> {
			if (file.isJsonObject())
				parseServer(file.getAsJsonObject(), location);
		});
		Arcana.logger.info("parsed researches on server");
	}

	public static void applyClient(Map<ResourceLocation, JsonElement> files){
		ClientResearchManager.books.clear();
		Requirement.initParser();
		BookSection.initParser();
		files.forEach((location, file) -> {
			if (file.isJsonObject())
				parseClient(file.getAsJsonObject(), location);
		});
		files.forEach((location, file) -> {
			if (file.isJsonObject())
				parseParents(file.getAsJsonObject(), location);
		});
		Arcana.logger.info("parsed researches on client");
	}

	static void parseServer(JsonObject json, ResourceLocation rl){
		try {
			int ctr = 0;
			for (JsonElement el : json.get("sections").getAsJsonArray()){
				JsonObject section = el.getAsJsonObject();
				if (section.get("type").getAsString().equals("requirements")){
					ResourceLocation key = new ResourceLocation(rl.getNamespace(), rl.getPath() + "/" + ++ctr);
					List<Requirement> reqs = Requirement.parse(section);
					ServerResearchManager.requirements.put(key, reqs);
				}
			}
		} catch (Exception e){
			Arcana.logger.error("Error parsing research file " + rl + " on server.");
			e.printStackTrace();
		}
	}

	static void parseClient(JsonObject json, ResourceLocation rl){
		try {
			int ctr = 0;
			BookEntry entry = new BookEntry();

			List<BookSection> sections = new ArrayList<>();
			for (JsonElement el : json.get("sections").getAsJsonArray()) {
				JsonObject section = el.getAsJsonObject();
				sections.add(BookSection.parse(section));
			}
			entry.sections = sections.toArray(new BookSection[0]);

			entry.x = json.get("x").getAsByte();
			entry.y = json.get("y").getAsByte();

			List<Item> icons = new ArrayList<>();
			json.get("icons").getAsJsonArray().forEach(el -> icons.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(el.getAsString()))));
			entry.icons = icons.toArray(new Item[0]);

			ClientResearchManager.add(rl, entry);
		} catch (Exception e){
			Arcana.logger.error("Error parsing research file " + rl + " on client.");
			e.printStackTrace();
		}
	}

	static void parseParents(JsonObject json, ResourceLocation rl){
		try {
			BookEntry current = ClientResearchManager.get(rl);
			if (!json.has("parents")){
				current.parents = ImmutableList.of();
				return;
			}
			List<BookEntry> parents = new ArrayList<>();
			json.get("parents").getAsJsonArray().forEach(el -> parents.add(ClientResearchManager.get(new ResourceLocation(el.getAsString()))));
			current.parents = ImmutableList.copyOf(parents);
		} catch (Exception e){
			Arcana.logger.error("Error parsing research file " + rl + " on client.");
			e.printStackTrace();
		}
	}
}