package com.sarraf.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.sarraf.app.data.local.dao.ExchangeRateDao;
import com.sarraf.app.data.local.dao.ExchangeRateDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ExchangeRateDao _exchangeRateDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `exchange_rates` (`id` TEXT NOT NULL, `baseCurrency` TEXT NOT NULL, `currencyCode` TEXT NOT NULL, `currencyName` TEXT NOT NULL, `currencySymbol` TEXT NOT NULL, `flag` TEXT NOT NULL, `rate` REAL NOT NULL, `change24h` REAL NOT NULL, `sparklineJson` TEXT NOT NULL, `category` TEXT NOT NULL, `subtitle` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rate_alerts` (`id` TEXT NOT NULL, `baseCode` TEXT NOT NULL, `targetCode` TEXT NOT NULL, `targetRate` REAL NOT NULL, `currentRate` REAL NOT NULL, `conditionType` TEXT NOT NULL, `isEnabled` INTEGER NOT NULL, `triggerFrequency` TEXT NOT NULL, `deliveryChannels` TEXT NOT NULL, `lastTriggered` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '31ec06a02ee6e661952e7ef95b60f88d')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `exchange_rates`");
        db.execSQL("DROP TABLE IF EXISTS `rate_alerts`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsExchangeRates = new HashMap<String, TableInfo.Column>(12);
        _columnsExchangeRates.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExchangeRates.put("baseCurrency", new TableInfo.Column("baseCurrency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExchangeRates.put("currencyCode", new TableInfo.Column("currencyCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExchangeRates.put("currencyName", new TableInfo.Column("currencyName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExchangeRates.put("currencySymbol", new TableInfo.Column("currencySymbol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExchangeRates.put("flag", new TableInfo.Column("flag", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExchangeRates.put("rate", new TableInfo.Column("rate", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExchangeRates.put("change24h", new TableInfo.Column("change24h", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExchangeRates.put("sparklineJson", new TableInfo.Column("sparklineJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExchangeRates.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExchangeRates.put("subtitle", new TableInfo.Column("subtitle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExchangeRates.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExchangeRates = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExchangeRates = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExchangeRates = new TableInfo("exchange_rates", _columnsExchangeRates, _foreignKeysExchangeRates, _indicesExchangeRates);
        final TableInfo _existingExchangeRates = TableInfo.read(db, "exchange_rates");
        if (!_infoExchangeRates.equals(_existingExchangeRates)) {
          return new RoomOpenHelper.ValidationResult(false, "exchange_rates(com.sarraf.app.data.local.entity.ExchangeRateEntity).\n"
                  + " Expected:\n" + _infoExchangeRates + "\n"
                  + " Found:\n" + _existingExchangeRates);
        }
        final HashMap<String, TableInfo.Column> _columnsRateAlerts = new HashMap<String, TableInfo.Column>(10);
        _columnsRateAlerts.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRateAlerts.put("baseCode", new TableInfo.Column("baseCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRateAlerts.put("targetCode", new TableInfo.Column("targetCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRateAlerts.put("targetRate", new TableInfo.Column("targetRate", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRateAlerts.put("currentRate", new TableInfo.Column("currentRate", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRateAlerts.put("conditionType", new TableInfo.Column("conditionType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRateAlerts.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRateAlerts.put("triggerFrequency", new TableInfo.Column("triggerFrequency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRateAlerts.put("deliveryChannels", new TableInfo.Column("deliveryChannels", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRateAlerts.put("lastTriggered", new TableInfo.Column("lastTriggered", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRateAlerts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRateAlerts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRateAlerts = new TableInfo("rate_alerts", _columnsRateAlerts, _foreignKeysRateAlerts, _indicesRateAlerts);
        final TableInfo _existingRateAlerts = TableInfo.read(db, "rate_alerts");
        if (!_infoRateAlerts.equals(_existingRateAlerts)) {
          return new RoomOpenHelper.ValidationResult(false, "rate_alerts(com.sarraf.app.data.local.entity.RateAlertEntity).\n"
                  + " Expected:\n" + _infoRateAlerts + "\n"
                  + " Found:\n" + _existingRateAlerts);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "31ec06a02ee6e661952e7ef95b60f88d", "f3e8befe182005661d4f3b6ba9ade659");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "exchange_rates","rate_alerts");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `exchange_rates`");
      _db.execSQL("DELETE FROM `rate_alerts`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ExchangeRateDao.class, ExchangeRateDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ExchangeRateDao exchangeRateDao() {
    if (_exchangeRateDao != null) {
      return _exchangeRateDao;
    } else {
      synchronized(this) {
        if(_exchangeRateDao == null) {
          _exchangeRateDao = new ExchangeRateDao_Impl(this);
        }
        return _exchangeRateDao;
      }
    }
  }
}
