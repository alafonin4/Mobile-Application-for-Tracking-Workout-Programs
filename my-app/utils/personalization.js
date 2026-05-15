export const combineAchievements = (...achievementGroups) => {
  return achievementGroups.flatMap((group) => group ?? []);
};

export const summarizeAchievements = (achievements = []) => {
  const totalCount = achievements.length;
  const unlockedCount = achievements.filter((item) => item?.unlocked).length;

  return {
    achievements,
    totalCount,
    unlockedCount,
  };
};
