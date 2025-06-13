import imageRoutes from './image.routes.js'
import notificationRoutes from './notification.routes.js'
import passwordResetRoutes from './password_reset.routes.js'
import userRoutes from './user.routes.js'
import transactionRoutes from './transaction.routes.js'
import categoryRoutes from './category.routes.js'
import chapterRoutes from './chapter.routes.js'
import storyRoutes from './story.routes.js'
import roleRoutes from './role.routes.js'
import adminRoutes from './admin.route.js'

function route(app) {
  app.use('/api/images', imageRoutes)
  app.use('/api/users', userRoutes)
  app.use('/api/admins', adminRoutes)
  app.use('/api/categories', categoryRoutes)
  app.use('/api/transactions', transactionRoutes)
  app.use('/api/notifications', notificationRoutes)
  app.use('/api/passwordResets', passwordResetRoutes)
  app.use('/api/stories', storyRoutes)
  app.use('/api/chapters', chapterRoutes)
  app.use('/api/roles', roleRoutes)
}

export default route
