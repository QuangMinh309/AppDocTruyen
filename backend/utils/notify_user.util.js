
import { clients } from "../websocket/socket.js";

export function notifyUser(userIds) {
  const userIdList = Array.isArray(userIds) ? userIds : [userIds];

  userIdList.forEach((userId) => {
    // console.log("thông báo mới")
    const ws = clients.get(userId)?.get('/ws/notification');
    if (ws && ws.readyState === ws.OPEN) {
      ws.send(JSON.stringify({
       action: 'BRC_NOTIFICATION',
        payload: "Bạn có một thông báo mới!!!"
      }));
    }
  });
}