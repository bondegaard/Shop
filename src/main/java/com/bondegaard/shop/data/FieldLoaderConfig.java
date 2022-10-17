package com.bondegaard.shop.data;

import com.bondegaard.shop.pluginmodule.ConfigOption;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

public class FieldLoaderConfig extends AbstractConfig {

    private final Object targetInstance;

    public FieldLoaderConfig(Object targetInstance, Plugin plugin, String resourcePath) {
        super(plugin, resourcePath);
        this.targetInstance = targetInstance;
    }

    public void loadConfigOptionFields() {
        for (Field field : this.targetInstance.getClass().getDeclaredFields())
            this.handleOptionFieldLoad(field);
    }

    private void handleOptionFieldLoad(Field field) {
        try {
            this.loadOptionValueIntoField(field);
        } catch (NullPointerException e) {
            this.plugin.getLogger().warning(String.format("Missing config value for field %s.", field.getName()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            this.plugin.getLogger().warning(String.format("Could not set field %s to be accessible.", field.getName()));
        }
    }

    private void loadOptionValueIntoField(Field targetField) throws NullPointerException, IllegalAccessException, NoSuchFieldException {
        targetField.setAccessible(true);

        if (!targetField.isAnnotationPresent(ConfigOption.class)) return;
        ConfigOption fieldConfigOption = targetField.getAnnotation(ConfigOption.class);

        String fullPath = this.fullPathFromFieldConfigOption(targetField, fieldConfigOption);

        if (!this.fileConfiguration.contains(fullPath))
            throw new NullPointerException(fullPath + " is not set in " + this.fileConfiguration.getName());

        Object configValue = this.parseConfigValue(targetField, fullPath);
        targetField.set(this.targetInstance, configValue);
    }

    private String fullPathFromFieldConfigOption(Field field, ConfigOption configOption) {
        String optionName = configOption.optionName().isEmpty() ? field.getName() : configOption.optionName();
        String path = configOption.path().isEmpty() ? configOption.path() : configOption.path() + ".";

        return "options." + path + optionName;
    }

    private Object parseConfigValue(Field targetField, String fullPath) throws IllegalAccessException {
        Class<?> fieldType = targetField.getType();

        if (fieldType.isAssignableFrom(ItemStack.class))
            return ItemConverter.parseItem(this.fileConfiguration.getConfigurationSection(fullPath));

        if (fieldType.isAssignableFrom(List.class) && targetField.getGenericType() instanceof ParameterizedType) {
            ParameterizedType listGenerics = (ParameterizedType) targetField.getGenericType();
            Class<?> primaryTypeClass = (Class<?>) listGenerics.getActualTypeArguments()[0];

            if (primaryTypeClass.isEnum()) {
                Class<Enum> primaryTypeAsEnum = (Class<Enum>) primaryTypeClass;

                return this.fileConfiguration.getStringList(fullPath)
                    .stream()
                    .map(enumName -> Enum.valueOf(primaryTypeAsEnum, enumName))
                    .collect(Collectors.toList());
            }
        }

        return this.fileConfiguration.get(fullPath);
    }

}
