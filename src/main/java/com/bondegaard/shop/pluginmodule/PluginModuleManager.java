package com.bondegaard.shop.pluginmodule;

import com.bondegaard.shop.Main;
import com.bondegaard.shop.pluginmodule.modules.shop.ShopModule;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PluginModuleManager {

    @Getter
    private final ClassToInstanceMap<PluginModule> modulesByClass = MutableClassToInstanceMap.create();

    private final Main plugin;

    public void initModules() {
        this.cleanOldModules();

        //Register the individual modules
        this.registerModule(new ShopModule(plugin, "shop"));

    }

    private void cleanOldModules() {
        this.modulesByClass.values().forEach(PluginModule::disableModule);
        this.modulesByClass.clear();
    }

    private void registerModule(PluginModule module) {
        if (this.modulesByClass.containsKey(module.getClass())) {
            this.plugin.getLogger().severe("Module " + module.getClass() + " has already been registered.");
            return;
        }

        this.modulesByClass.put(module.getClass(), module);
        this.errorProneEnable(module);
    }

    private void errorProneEnable(PluginModule module) {
        try {
            module.enableModule();
        } catch (Exception enableExc) {
            enableExc.printStackTrace();
            this.plugin.getLogger().severe("Module " + module.getName() + " could not be properly loaded. Trying to disable module now...");

            this.errorProneDisable(module);
        }
    }

    private void errorProneDisable(PluginModule module) {
        try {
            module.disableModule();
        } catch (Exception disableExc) {
            disableExc.printStackTrace();
            this.plugin.getLogger().severe("Module " + module.getName() + " is now disabled, but encountered an error in the process.");
        }
    }

    public <T extends PluginModule> T getModule(Class<T> moduleClass) {
        return this.modulesByClass.getInstance(moduleClass);
    }

    public void enableModule(Class<? extends PluginModule> module) {
        this.getModule(module).enableModule();
    }

    public void disableModule(Class<? extends PluginModule> module) {
        this.getModule(module).disableModule();
    }

    public void reloadModule(Class<? extends PluginModule> module) {
        this.getModule(module).reloadModule();
    }

    public void unloadModules() {
        this.cleanOldModules();
    }

}
