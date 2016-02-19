module.exports = function(grunt) {

  grunt.initConfig({

    uglify: {
      build: {
        src: ['public/javascripts/app/*.js'],
        dest: 'public/javascripts/lib/main.js'
      }
    },

    karma: {
      unit: { configFile: 'tests/karma.conf.js' }
    },

    watch: {
      options: {
        livereload: true
      },
      build: {
        files: ['public/javascripts/app/*.js', 'public/*.*', 'public/stylesheets/*.*', 'public/partials/*.*'],
        tasks: ['uglify']
      }
    },

  });

  grunt.loadNpmTasks('grunt-karma');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-uglify');

  grunt.registerTask('default', ['watch']);

};