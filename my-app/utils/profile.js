export const FITNESS_GOAL_LABELS = {
  GENERAL_FITNESS: "Общая форма",
  STRENGTH: "Рост силы",
  MUSCLE_GAIN: "Набор массы",
  WEIGHT_LOSS: "Снижение веса",
  ENDURANCE: "Выносливость",
};

export const FITNESS_GOALS = Object.entries(FITNESS_GOAL_LABELS).map(([value, label]) => ({
  value,
  label,
}));

export const getFitnessGoalLabel = (goal) => {
  if (!goal) {
    return "Общая форма";
  }

  return FITNESS_GOAL_LABELS[goal] ?? goal;
};
