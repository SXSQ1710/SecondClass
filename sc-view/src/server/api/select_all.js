// 操作数据库
let db = require("../db/index");

exports.get_org = (req, res) => {
  var sql =
    "SELECT t_organization.* , uname,phone " +
    "FROM t_organization,t_user " +
    "WHERE t_organization.uid = t_user.uid";
  db.query(
    sql,
    [
      req.query.uid,
      req.query.oid,
    ],
    (err, data) => {
      if (err) {
        return res.send("错误:" + err.message);
      }
      res.send(data);
    }
  );
};

exports.get_user = (req, res) => {
  var sql =
    "SELECT t_user.*,cname,oname FROM t_user,t_class,t_organization" +
    " WHERE t_user.oid  = t_organization.oid AND " +
    " t_user.cid = t_class.cid";
  db.query(
    sql,
    [
      req.query.uid,
      req.query.oid,
   ],
    (err, data) => {
      if (err) {
        return res.send("错误:" + err.message);
      }
      res.send(data);
    }
  );
};


exports.get_activity = (req, res) => {
  var sql =
    "SELECT t_activity.* , t_organization.oname FROM " +
    " t_activity , t_organization" +
    " WHERE t_activity.a_oid = t_organization.oid";
  db.query(
    sql,
    [
      req.query.aid,
      req.query.oname,
   ],
    (err, data) => {
      if (err) {
        return res.send("错误:" + err.message);
      }
      res.send(data);
    }
  );
};
