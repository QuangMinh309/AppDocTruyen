import fs from 'fs'
import path from 'path'
import { Sequelize, DataTypes } from 'sequelize'
import process from 'process'
import { fileURLToPath, pathToFileURL } from 'url'
import configFile from '../config/config.js'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)
const basename = path.basename(__filename)

const env = process.env.NODE_ENV || 'development'
const currconfig = configFile[env] || configFile['development']

const db = {}

let sequelize
if (currconfig.use_env_variable) {
  sequelize = new Sequelize(
    process.env[currconfig.use_env_variable],
    currconfig
  )
} else {
  sequelize = new Sequelize(
    currconfig.database,
    currconfig.username,
    currconfig.password,
    {
      ...currconfig,
      logging: currconfig.logging ?? false,
    }
  )
}

try {
  // Đọc các file model từ thư mục entities
  const entitiesDir = path.join(__dirname, 'entities')
  let entitiesFiles = []
  if (fs.existsSync(entitiesDir)) {
    entitiesFiles = fs.readdirSync(entitiesDir).filter((file) => {
      return (
        file.indexOf('.') !== 0 &&
        file !== basename &&
        file.endsWith('.js') &&
        !file.endsWith('.test.js')
      )
    })
  } else {
    console.warn('Thư mục entities không tồn tại:', entitiesDir)
  }

  // Load models từ thư mục entities
  for (const file of entitiesFiles) {
    const filePath = path.join(entitiesDir, file)
    const fileUrl = pathToFileURL(filePath).href
    const entitiesModule = await import(fileUrl)
    const model = entitiesModule.default(sequelize, DataTypes)
    db[model.name] = model
  }

  // Đọc các file model từ thư mục relations
  const relationsDir = path.join(__dirname, 'relations')
  let relationFiles = []
  if (fs.existsSync(relationsDir)) {
    relationFiles = fs.readdirSync(relationsDir).filter((file) => {
      return (
        file.indexOf('.') !== 0 &&
        file !== basename &&
        file.endsWith('.js') &&
        !file.endsWith('.test.js')
      )
    })
  } else {
    console.warn('Thư mục relations không tồn tại:', relationsDir)
  }

  // Load models từ thư mục relations
  for (const file of relationFiles) {
    const filePath = path.join(relationsDir, file)
    const fileUrl = pathToFileURL(filePath).href
    const relationModule = await import(fileUrl)
    const model = relationModule.default(sequelize, DataTypes)
    db[model.name] = model
  }

  // Thiết lập quan hệ giữa các model
  for (const modelName of Object.keys(db)) {
    if (typeof db[modelName].associate === 'function') {
      db[modelName].associate(db)
    }
  }
} catch (error) {
  console.error('Lỗi khi tải các model:', error)
  throw error
}

db.sequelize = sequelize
db.Sequelize = Sequelize

export default db
export const models = db
export { sequelize }
