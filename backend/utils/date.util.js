export const formatDate = (date, type = 'yyyy-mm-dd') => {
  if (!date) return null;
  const d = new Date(date);

  if (type === 'iso') return d.toISOString();
  if (type === 'locale') return d.toLocaleDateString();
  return d.toISOString().split('T')[0];
};
