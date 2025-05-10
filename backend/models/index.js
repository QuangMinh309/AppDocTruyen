import fs from 'fs';
import path from 'path';
import { Sequelize, DataTypes } from 'sequelize';
import process from 'process';
import { fileURLToPath } from 'url';
import configFile from '../config/config.js'; // Import file config.js

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const basename = path.basename(__filename);

const env = process.env.NODE_ENV || 'development';
const currconfig = configFile[env]; // Lấy đúng environment config

const db = {};


//console.log("DB_DIALECT:", process.env.DB_HOST); // Dòng này để debug
let sequelize;

if (currconfig.use_env_variable) {
  sequelize = new Sequelize(process.env[currconfig.use_env_variable], currconfig);
} else {
  sequelize = new Sequelize(currconfig.database, currconfig.username, currconfig.password, {
    ...currconfig,
    logging: false,
  });
}

// Đọc các file model
const files = fs
  .readdirSync(__dirname)
  .filter((file) => {
    return (
      file.indexOf('.') !== 0 &&
      file !== basename &&
      file.endsWith('.js') &&
      !file.endsWith('.test.js')
    );
  });

for (const file of files) {
  const modelModule = await import(path.join(__dirname, file));
  const model = modelModule.default(sequelize, DataTypes);
  db[model.name] = model;
}

for (const modelName of Object.keys(db)) {
  if (db[modelName].associate) {
    db[modelName].associate(db);
  }
}

db.sequelize = sequelize;
db.Sequelize = Sequelize;

export default db;
