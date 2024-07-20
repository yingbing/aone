document.addEventListener("DOMContentLoaded", function() {
    new Vue({
        el: '#app',
        data: {
            message: '',
            selectedEnvironment: '',
            environments: [],
            databases: [],
            selectedDatabase: '',
            serverResponse: '' // 新增用于保存服务器响应的变量
        },
        created() {
            fetch('/api/message')
                .then(response => response.json())
                .then(data => {
                    this.message = data.message;
                });
            fetch('/api/environments')
                .then(response => response.json())
                .then(data => {
                    this.environments = data.map(env => ({
                        value: env,
                        label: this.translateEnvironment(env)
                    }));
                });
            this.loadDatabases(); // 初始化加载数据库列表
        },
        methods: {
            showMessage() {
                this.$message('Hello from Element UI!');
            },
            translateEnvironment(env) {
                switch(env) {
                    case 'development': return '开发环境';
                    case 'testing': return '测试环境';
                    case 'production': return '生产环境';
                    default: return env;
                }
            },
            loadDatabases() {
                fetch('/api/databases')
                    .then(response => response.json())
                    .then(data => {
                        this.databases = data;
                    });
            },
            reloadDB() {
                if (this.selectedDatabase) {
                    fetch('/api/databases', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ database: this.selectedDatabase }) // 确保以对象格式发送
                    })
                    .then(response => response.json())
                    .then(data => {
                        this.serverResponse = `Status: ${data.status}`; // 更新服务器响应的显示
                        this.$message(`Server response: ${data.status}`);
                    }).catch(error => {
                        console.error("Error:", error);
                        this.$message.error("Failed to reload database");
                    });
                } else {
                    this.$message('Please select a database to reload');
                }
            }
        }
    });
});
