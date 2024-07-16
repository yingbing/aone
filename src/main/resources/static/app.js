document.addEventListener("DOMContentLoaded", function() {
    new Vue({
        el: '#app',
        data: {
            message: '',
            selectedEnvironment: '',
            environments: [],
            databases: [],
            selectedDatabase: ''
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
            this.reloadDB(); // 初始化加载数据库列表
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
            reloadDB() {
                fetch('/api/databases')
                    .then(response => response.json())
                    .then(data => {
                        this.databases = data;
                    });
            }
        }
    });
});
