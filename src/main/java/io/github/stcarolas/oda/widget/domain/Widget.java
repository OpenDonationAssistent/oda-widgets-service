package io.github.stcarolas.oda.widget.domain;

import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Serdeable
@MappedEntity("widget")
public class Widget extends WidgetData {

  public Map<String, Object> getConfig() {
    var config = super.getConfig();
    if (config == null || config.isEmpty()) {
      return Map.of("properties", List.of());
    }
    return config;
  }

  private List<Map<String, Object>> properties() {
    var config = getConfig();
    List<Object> properties = (List) config.getOrDefault(
      "properties",
      List.<Object>of()
    );
    return properties.stream().map(it -> (Map<String, Object>) it).toList();
  }

  public boolean hasNoProperties() {
    return properties().isEmpty();
  }

  public Optional<Map<String, Object>> getProperty(String name) {
    return properties()
      .stream()
      .filter(prop -> name.equals(prop.get("name")))
      .findFirst();
  }

  public <T> Optional<T> getValue(String name) {
    return getProperty(name)
      .map(prop -> prop.get("value"))
      .map(value -> (T) value);
  }

  public Widget removeProperty(String name) {
    var updatedProperties = properties()
      .stream()
      .filter(prop -> !name.equals(((Map<String, Object>) prop).get("name")))
      .toList();
    var updatedConfig = new HashMap<String, Object>();
    updatedConfig.putAll(getConfig());
    updatedConfig.put("properties", updatedProperties);
    return withConfig(updatedConfig);
  }

  public Widget updateProperty(String name, Object value) {
    final List<Map<String, Object>> updatedProperties = properties()
      .stream()
      .map(property ->
        name.equals(property.get("name"))
          ? Map.<String, Object>of("name", name, "value", value)
          : property
      )
      .toList();
    var updatedConfig = new HashMap<String, Object>();
    updatedConfig.putAll(getConfig());
    updatedConfig.put("properties", updatedProperties);
    return withConfig(updatedConfig);
  }

  private Widget withConfig(Map<String, Object> config) {
    var result = new Widget();
    result.setId(getId());
    result.setName(getName());
    result.setType(getType());
    result.setOwnerId(getOwnerId());
    result.setSortOrder(getSortOrder());
    result.setConfig(config);
    return result;
  }
}
