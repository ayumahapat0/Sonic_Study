from flask import Flask, jsonify, request
import mysql.connector
from mysql.connector import Error
from flask_cors import CORS

app = Flask(__name__)

CORS(app)

# Database configuration

DB_CONFIG = {
    'host': 'awsdatabase.cxo2wiu4udmp.us-east-2.rds.amazonaws.com',  # Replace with your RDS endpoint
    'user': 'admin',                   # Replace with your RDS username
    'password': 'ui3cBo6mRwcaXDEMo8OD',               # Replace with your RDS password
    'database': 'awsdatabase'           # Replace with your database name
}

# Connect to the database
def create_connection():
    try:
        connection = mysql.connector.connect(
            host=DB_CONFIG['host'],
            user=DB_CONFIG['user'],
            password=DB_CONFIG['password'],
            database=DB_CONFIG['database']
        )
        if connection.is_connected():
            print("yay we can connect")
            return connection
    except Error as e:
        print(f"Error while connecting to MySQL: {e}")
        return None

@app.route('/create_table', methods=['POST'])
def create_table():
    data = request.json
    table_name = data['tableName']
    columns = data['columns']
    primary_key = data['primaryKey']

    connection = create_connection()
    if not connection:
        return jsonify({"error": "Failed to connect to the database."}), 500

    try:
        # Modify the column definitions to ensure the primary key is AUTO_INCREMENT
        column_definitions = []
        print(columns)
        column_definitions.append(f"{columns[0]} AUTO_INCREMENT")
        for i in range(1, len(columns)):
            column_definitions.append(columns[i])
        
        print(column_definitions)
        column_definitions_str = ", ".join(column_definitions[:3])
        print(column_definitions_str)
        query = f"CREATE TABLE IF NOT EXISTS {table_name} ({column_definitions_str}, PRIMARY KEY ({column_definitions[3]}))"

        with connection.cursor() as cursor:
            cursor.execute(query)
            connection.commit()
        return jsonify({"message": "Table created successfully."})
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        connection.close()

@app.route('/insert_data', methods=['POST'])
def insert_data():
    data = request.json
    table_name = data['tableName']
    question = data['question']
    answer = data['answer']

    connection = create_connection()
    if not connection:
        return jsonify({"error": "Failed to connect to the database."}), 500

    try:
        query = f"INSERT INTO {table_name} (question, answer) VALUES (%s, %s)"
        with connection.cursor() as cursor:
            cursor.execute(query, (question, answer))
            connection.commit()
        return jsonify({"message": "Data inserted successfully."})
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        connection.close()

@app.route('/delete_data', methods=['POST'])
def delete_data():
    data = request.json
    table_name = data['tableName']
    condition = data['condition']

    connection = create_connection()
    if not connection:
        return jsonify({"error": "Failed to connect to the database."}), 500

    try:
        query = f"DELETE FROM {table_name} WHERE {condition}"
        with connection.cursor() as cursor:
            cursor.execute(query)
            connection.commit()
        return jsonify({"message": "Data deleted successfully."})
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        connection.close()

@app.route('/update_data', methods=['POST'])
def update_data():
    data = request.json
    table_name = data['tableName']
    question = data['question']
    answer = data['answer']
    condition = data['condition']

    connection = create_connection()
    if not connection:
        return jsonify({"error": "Failed to connect to the database."}), 500

    try:
        query = f"UPDATE {table_name} SET question = %s, answer = %s WHERE {condition}"
        with connection.cursor() as cursor:
            cursor.execute(query, (question, answer))
            connection.commit()
        return jsonify({"message": "Data updated successfully."})
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        connection.close()

@app.route('/retrieve_data', methods=['POST'])
def retrieve_data():
    data = request.json
    table_name = data['tableName']
    # Get rid of condition aspect

    connection = create_connection()
    if not connection:
        return jsonify({"error": "Failed to connect to the database."}), 500

    try:
        # Get rid of condition aspect
        query = f"SELECT * FROM {table_name}"
        with connection.cursor() as cursor:
            cursor.execute(query)
            columns = [desc[0] for desc in cursor.description]
            rows = cursor.fetchall()
            result = [dict(zip(columns, row)) for row in rows]
        return jsonify(result)
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        connection.close()

@app.route('/retrieve_tables', methods=['POST'])
def retrieve_tables():
    connection = create_connection()
    if not connection:
        return jsonify({"error": "Failed to connect to the database."}), 500

    try:
        query = "SHOW TABLES"
        with connection.cursor() as cursor:
            cursor.execute(query)
            tables = [table[0] for table in cursor.fetchall()]
        return jsonify(tables)
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        connection.close()


@app.route('/delete_table', methods=['POST'])
def delete_table():
    data = request.json
    table_name = data['tableName']

    connection = create_connection()
    if not connection:
        return jsonify({"error": "Failed to connect to the database."}), 500

    try:
        query = f"DROP TABLE IF EXISTS {table_name}"
        with connection.cursor() as cursor:
            cursor.execute(query)
            connection.commit()
        return jsonify({"message": "Table deleted successfully."})
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        connection.close()

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8080)