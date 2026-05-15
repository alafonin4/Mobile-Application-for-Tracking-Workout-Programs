import React, { useEffect, useMemo, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  FlatList,
  Modal,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { Feather } from "@expo/vector-icons";

import { getApiErrorMessage } from "../api/client";
import { getExerciseCatalog } from "../api/exercises/getExerciseCatalog";
import {
  addFavoriteExercise,
  removeFavoriteExercise,
} from "../api/exercises/toggleFavoriteExercise";

const WEIGHT_FILTERS = [
  { key: "all", label: "Все" },
  { key: "withWeight", label: "С доп. весом" },
  { key: "withoutWeight", label: "Без доп. веса" },
];

const getWeightFilterValue = (weightFilter) => {
  if (weightFilter === "withWeight") {
    return true;
  }
  if (weightFilter === "withoutWeight") {
    return false;
  }
  return null;
};

const AddExerciseModal = ({ visible, onClose, onSelectExercise, userId }) => {
  const [catalog, setCatalog] = useState([]);
  const [loading, setLoading] = useState(false);
  const [updatingFavoriteId, setUpdatingFavoriteId] = useState(null);
  const [selectedMuscleGroup, setSelectedMuscleGroup] = useState("all");
  const [selectedWeightFilter, setSelectedWeightFilter] = useState("all");

  useEffect(() => {
    if (!visible || userId == null) {
      return;
    }

    let isMounted = true;

    const loadCatalog = async () => {
      setLoading(true);
      try {
        const data = await getExerciseCatalog(userId);
        if (isMounted) {
          setCatalog(data);
        }
      } catch (error) {
        if (isMounted) {
          setCatalog([]);
        }
        if (visible) {
          Alert.alert(
            "Ошибка",
            getApiErrorMessage(error, "Не удалось загрузить каталог упражнений.")
          );
        }
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };

    loadCatalog();

    return () => {
      isMounted = false;
    };
  }, [visible, userId]);

  const muscleGroups = useMemo(() => {
    const unique = Array.from(
      new Set(catalog.map((item) => item.muscleGroup).filter(Boolean))
    ).sort((left, right) => left.localeCompare(right));

    return ["all", ...unique];
  }, [catalog]);

  const exerciseList = useMemo(() => {
    const requiredWeightValue = getWeightFilterValue(selectedWeightFilter);

    return catalog
      .filter((item) =>
        selectedMuscleGroup === "all"
          ? true
          : item.muscleGroup?.toLowerCase() === selectedMuscleGroup.toLowerCase()
      )
      .filter((item) =>
        requiredWeightValue === null
          ? true
          : Boolean(item.requiresAdditionalWeight) === requiredWeightValue
      )
      .sort((left, right) => {
        if (Boolean(left.favorite) !== Boolean(right.favorite)) {
          return left.favorite ? -1 : 1;
        }
        return left.name.localeCompare(right.name);
      });
  }, [catalog, selectedMuscleGroup, selectedWeightFilter]);

  const handleToggleFavorite = async (exercise) => {
    if (userId == null || updatingFavoriteId != null) {
      return;
    }

    setUpdatingFavoriteId(exercise.id);
    try {
      if (exercise.favorite) {
        await removeFavoriteExercise(exercise.id, userId);
      } else {
        await addFavoriteExercise(exercise.id, userId);
      }

      setCatalog((current) =>
        current.map((item) =>
          item.id === exercise.id ? { ...item, favorite: !item.favorite } : item
        )
      );
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось обновить избранные упражнения.")
      );
    } finally {
      setUpdatingFavoriteId(null);
    }
  };

  return (
    <Modal visible={visible} transparent animationType="slide" onRequestClose={onClose}>
      <View style={styles.overlay}>
        <View style={styles.modal}>
          <Text style={styles.title}>Выберите упражнение</Text>

          <Text style={styles.filterTitle}>Группа мышц</Text>
          <FlatList
            horizontal
            data={muscleGroups}
            keyExtractor={(item) => item}
            showsHorizontalScrollIndicator={false}
            contentContainerStyle={styles.filtersRow}
            renderItem={({ item }) => (
              <TouchableOpacity
                style={[
                  styles.filterChip,
                  selectedMuscleGroup === item && styles.filterChipActive,
                ]}
                onPress={() => setSelectedMuscleGroup(item)}
              >
                <Text
                  style={[
                    styles.filterChipText,
                    selectedMuscleGroup === item && styles.filterChipTextActive,
                  ]}
                >
                  {item === "all" ? "Все" : item}
                </Text>
              </TouchableOpacity>
            )}
          />

          <Text style={styles.filterTitle}>Дополнительный вес</Text>
          <View style={styles.weightFiltersRow}>
            {WEIGHT_FILTERS.map((filter) => (
              <TouchableOpacity
                key={filter.key}
                style={[
                  styles.filterChip,
                  selectedWeightFilter === filter.key && styles.filterChipActive,
                ]}
                onPress={() => setSelectedWeightFilter(filter.key)}
              >
                <Text
                  style={[
                    styles.filterChipText,
                    selectedWeightFilter === filter.key && styles.filterChipTextActive,
                  ]}
                >
                  {filter.label}
                </Text>
              </TouchableOpacity>
            ))}
          </View>

          {loading ? (
            <ActivityIndicator size="large" color="#72bcd4" />
          ) : (
            <FlatList
              data={exerciseList}
              keyExtractor={(item) => item.id.toString()}
              renderItem={({ item }) => (
                <View style={styles.item}>
                  <TouchableOpacity
                    style={styles.itemContent}
                    onPress={() => onSelectExercise(item)}
                  >
                    <Text style={styles.itemText}>{item.name}</Text>
                    <Text style={styles.groupText}>
                      {item.muscleGroup || "Без группы"} •{" "}
                      {item.requiresAdditionalWeight ? "с доп. весом" : "без доп. веса"}
                    </Text>
                  </TouchableOpacity>

                  <TouchableOpacity
                    style={styles.favoriteButton}
                    onPress={() => handleToggleFavorite(item)}
                    disabled={updatingFavoriteId === item.id}
                  >
                    <Feather
                      name={item.favorite ? "star" : "star"}
                      size={18}
                      color={item.favorite ? "#FBBF24" : "#64748B"}
                    />
                    <Text
                      style={[
                        styles.favoriteText,
                        item.favorite && styles.favoriteTextActive,
                      ]}
                    >
                      {updatingFavoriteId === item.id
                        ? "..."
                        : item.favorite
                        ? "Избранное"
                        : "В избранное"}
                    </Text>
                  </TouchableOpacity>
                </View>
              )}
              ListEmptyComponent={
                <Text style={styles.emptyText}>Упражнения по выбранным фильтрам не найдены.</Text>
              }
            />
          )}

          <TouchableOpacity onPress={onClose} style={styles.cancel}>
            <Text style={styles.cancelText}>Отмена</Text>
          </TouchableOpacity>
        </View>
      </View>
    </Modal>
  );
};

export default AddExerciseModal;

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: "rgba(0,0,0,0.5)",
    justifyContent: "center",
    paddingHorizontal: 20,
  },
  modal: {
    backgroundColor: "#222",
    borderRadius: 12,
    padding: 20,
    maxHeight: "85%",
  },
  title: {
    color: "#fff",
    fontSize: 18,
    marginBottom: 12,
    fontWeight: "600",
  },
  filterTitle: {
    color: "#CBD5E1",
    fontSize: 13,
    marginTop: 4,
    marginBottom: 8,
    fontWeight: "600",
  },
  filtersRow: {
    gap: 8,
    paddingBottom: 10,
  },
  weightFiltersRow: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 8,
    marginBottom: 12,
  },
  filterChip: {
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 999,
    backgroundColor: "#374151",
  },
  filterChipActive: {
    backgroundColor: "#2563EB",
  },
  filterChipText: {
    color: "#E5E7EB",
    fontSize: 12,
    fontWeight: "600",
  },
  filterChipTextActive: {
    color: "#fff",
  },
  item: {
    paddingVertical: 12,
    borderBottomWidth: 1,
    borderBottomColor: "#333",
  },
  itemContent: {
    marginBottom: 8,
  },
  itemText: {
    color: "#fff",
    fontSize: 16,
  },
  groupText: {
    color: "#999",
    marginTop: 4,
  },
  favoriteButton: {
    flexDirection: "row",
    alignItems: "center",
    alignSelf: "flex-start",
    gap: 8,
  },
  favoriteText: {
    color: "#94A3B8",
    fontSize: 12,
    fontWeight: "600",
  },
  favoriteTextActive: {
    color: "#FBBF24",
  },
  emptyText: {
    color: "#999",
    textAlign: "center",
    paddingVertical: 24,
  },
  cancel: {
    marginTop: 12,
    alignItems: "center",
  },
  cancelText: {
    color: "#d33",
    fontSize: 16,
  },
});
