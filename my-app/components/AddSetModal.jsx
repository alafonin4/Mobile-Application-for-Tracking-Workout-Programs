import React, { useState } from 'react';
import { Modal, View, Text, TextInput, TouchableOpacity, StyleSheet } from 'react-native';

const AddSetModal = ({ visible, onClose, onAddSet }) => {
  const [reps, setReps] = useState('');
  const [weight, setWeight] = useState('');

  const handleAdd = () => {
    const repsNumber = parseInt(reps, 10);
    const weightNumber = parseFloat(weight);
    if (!isNaN(repsNumber)) {
      onAddSet({ reps: repsNumber, weight: isNaN(weightNumber) ? 0 : weightNumber });
      setReps('');
      setWeight('');
      onClose();
    }
  };

  return (
    <Modal visible={visible} transparent animationType="slide">
      <View style={styles.overlay}>
        <View style={styles.modal}>
          <Text style={styles.title}>Добавить подход</Text>
          <TextInput
            style={styles.input}
            placeholder="Количество повторений"
            placeholderTextColor="#aaa"
            keyboardType="numeric"
            value={reps}
            onChangeText={setReps}
          />
          <TextInput
            style={styles.input}
            placeholder="Вес (введите 0 для веса тела)"
            placeholderTextColor="#aaa"
            keyboardType="numeric"
            value={weight}
            onChangeText={setWeight}
          />
          <TouchableOpacity onPress={handleAdd} style={styles.button}>
            <Text style={styles.buttonText}>Ок</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={onClose} style={styles.cancel}>
            <Text style={styles.cancelText}>Отмена</Text>
          </TouchableOpacity>
        </View>
      </View>
    </Modal>
  );
};

export default AddSetModal;

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'center',
    paddingHorizontal: 20,
  },
  modal: {
    backgroundColor: '#222',
    borderRadius: 12,
    padding: 20,
  },
  title: {
    color: '#fff',
    fontSize: 18,
    marginBottom: 12,
    fontWeight: '600',
  },
  input: {
    backgroundColor: '#333',
    color: '#fff',
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
    marginBottom: 12,
  },
  button: {
    backgroundColor: '#4caf50',
    padding: 12,
    borderRadius: 8,
    alignItems: 'center',
    marginBottom: 8,
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
  },
  cancel: {
    alignItems: 'center',
  },
  cancelText: {
    color: '#f44336',
    fontSize: 16,
  },
});