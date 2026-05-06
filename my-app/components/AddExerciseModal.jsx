import React, { useEffect, useMemo, useState } from "react";
import {
  ActivityIndicator,
  FlatList,
  Modal,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { getAllExercises } from "../api/exercises/getAllExercises";

const AddExerciseModal = ({
  visible,
  onClose,
  onSelectExercise,
  exercises = null,
}) => {
  const [loadedExercises, setLoadedExercises] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!visible || Array.isArray(exercises)) {
      return;
    }

    let isMounted = true;

    const loadExercises = async () => {
      setLoading(true);
      const data = await getAllExercises();
      if (isMounted) {
        setLoadedExercises(data);
        setLoading(false);
      }
    };

    loadExercises();

    return () => {
      isMounted = false;
    };
  }, [visible, exercises]);

  const exerciseList = useMemo(() => {
    if (Array.isArray(exercises)) {
      return exercises;
    }
    return loadedExercises;
  }, [exercises, loadedExercises]);

  return (
    <Modal visible={visible} transparent animationType="slide" onRequestClose={onClose}>
      <View style={styles.overlay}>
        <View style={styles.modal}>
          <Text style={styles.title}>Выберите упражнение</Text>

          {loading ? (
            <ActivityIndicator size="large" color="#72bcd4" />
          ) : (
            <FlatList
              data={exerciseList}
              keyExtractor={(item) => item.id.toString()}
              renderItem={({ item }) => (
                <TouchableOpacity
                  onPress={() => onSelectExercise(item)}
                  style={styles.item}
                >
                  <Text style={styles.itemText}>{item.name}</Text>
                  <Text style={styles.groupText}>{item.muscleGroup}</Text>
                </TouchableOpacity>
              )}
              ListEmptyComponent={
                <Text style={styles.emptyText}>Упражнения пока не найдены.</Text>
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
    maxHeight: "80%",
  },
  title: {
    color: "#fff",
    fontSize: 18,
    marginBottom: 12,
    fontWeight: "600",
  },
  item: {
    paddingVertical: 12,
    borderBottomWidth: 1,
    borderBottomColor: "#333",
  },
  itemText: {
    color: "#fff",
    fontSize: 16,
  },
  groupText: {
    color: "#999",
    marginTop: 4,
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
