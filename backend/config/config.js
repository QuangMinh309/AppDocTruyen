import dotenv from 'dotenv';
dotenv.config();

const commonConfig = {
  username: process.env.DB_USERNAME,
  password: process.env.DB_PASSWORD,
  host: process.env.DB_HOST,
  port: process.env.DB_PORT,
  dialect: process.env.DB_DIALECT,
  logging: false,
  timezone: '+07:00',
  dialectOptions:
    process.env.DB_HOST === 'localhost'
      ? {}
      : {
          ssl: {
            require: true,
            rejectUnauthorized: false,
          },
        },
};

const config = {
  development: {
    ...commonConfig,
    database: process.env.DB_NAME,
  },
  test: {
    ...commonConfig,
    database: process.env.DB_NAME + '_test',
  },
  // production: {
  //   ...commonConfig,
  //   database: process.env.DB_NAME,
  // },
};

export default config;
