import React, { useState, useEffect } from 'react';
import { 
  View, 
  Text, 
  TouchableOpacity, 
  FlatList, 
  StyleSheet, 
  ActivityIndicator 
} from 'react-native';
// import axios from 'axios'; 
//import { fetchCompetitionData } from '../api/competition/fetchCompetitionData';

const CompetitionScreen = () => {
  const [filter, setFilter] = useState('all');
  const [competitionData, setCompetitionData] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadCompetitionData();
  }, [filter]);

  const loadCompetitionData = async () => {
    setLoading(true);
    try {
      /*
      const response = await axios.get('http://your-api-url/api/competitions/current', { params: { filter } });
      setCompetitionData(response.data);
      */
      if (filter === 'all') {
        setCompetitionData([
          { id: 1, userName: 'Alice', performance: 5000 },
          { id: 2, userName: 'Bob', performance: 4500 },
          { id: 3, userName: 'Charlie', performance: 4000 },
        ]);
      } else {
        setCompetitionData([
          { id: 2, userName: 'Bob', performance: 4500 },
          { id: 3, userName: 'Charlie', performance: 4000 },
        ]);
      }
    } catch (error) {
      console.error('Ошибка при загрузке данных соревнования:', error);
      setCompetitionData([]);
    } finally {
      setLoading(false);
    }
  };

  const renderItem = ({ item, index }) => (
    <View style={styles.item}>
      <Text style={styles.rank}>{index + 1}.</Text>
      <Text style={styles.userName}>{item.userName}</Text>
      <Text style={styles.performance}>{item.performance}</Text>
    </View>
  );

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Ежемесячное соревнование</Text>
      <View style={styles.filterContainer}>
        <TouchableOpacity
          style={[styles.filterButton, filter === 'all' && styles.activeFilter]}
          onPress={() => setFilter('all')}
        >
          <Text style={[styles.filterText, filter === 'all' && styles.activeFilterText]}>
            Все пользователи
          </Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.filterButton, filter === 'friends' && styles.activeFilter]}
          onPress={() => setFilter('friends')}
        >
          <Text style={[styles.filterText, filter === 'friends' && styles.activeFilterText]}>
            Друзья
          </Text>
        </TouchableOpacity>
      </View>
      {loading ? (
        <ActivityIndicator size="large" color="#007BFF" />
      ) : competitionData && competitionData.length > 0 ? (
        <FlatList
          data={competitionData}
          keyExtractor={(item) => item.id.toString()}
          renderItem={renderItem}
          contentContainerStyle={styles.listContainer}
        />
      ) : (
        <View style={styles.emptyContainer}>
          <Text style={styles.emptyText}>
            Нет данных для отображения. Попробуйте изменить фильтр.
          </Text>
        </View>
      )}
    </View>
  );
};
const styles = StyleSheet.create({
    container: {
      flex: 1, 
      padding: 16, 
      backgroundColor: '#fff'
    },
    title: {
      fontSize: 24, 
      fontWeight: 'bold', 
      marginBottom: 16, 
      textAlign: 'center'
    },
    filterContainer: {
      flexDirection: 'row', 
      justifyContent: 'center', 
      marginBottom: 16
    },
    filterButton: {
      paddingVertical: 10,
      paddingHorizontal: 15,
      marginHorizontal: 5,
      borderWidth: 1,
      borderColor: '#007BFF',
      borderRadius: 5,
    },
    activeFilter: {
      backgroundColor: '#007BFF'
    },
    filterText: {
      color: '#007BFF',
      fontSize: 16,
    },
    activeFilterText: {
      color: '#fff'
    },
    listContainer: {
      paddingBottom: 20,
    },
    item: {
      flexDirection: 'row', 
      alignItems: 'center',
      paddingVertical: 12, 
      borderBottomWidth: 1, 
      borderBottomColor: '#ccc'
    },
    rank: {
      fontSize: 18, 
      fontWeight: 'bold', 
      width: 40
    },
    userName: {
      flex: 1, 
      fontSize: 16
    },
    performance: {
      fontSize: 16, 
      fontWeight: 'bold'
    },
    emptyContainer: {
      alignItems: 'center', 
      marginTop: 50
    },
    emptyText: {
      fontSize: 16, 
      color: 'gray'
    }
  });
  
  export default CompetitionScreen;