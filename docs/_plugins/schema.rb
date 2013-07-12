module Jekyll
    class IncludeSchema < Liquid::Tag
        REPO_URL = "https://github.com/enviroCar/enviroCar-server.git"
        LANG = "json"
        SCHEMA_PATH = "../rest/src/main/resources/schema"
        EXAMPLES_PATH = "_examples"
        def initialize(tag_name, text, token)
            super
            case tag_name
            when "include_schema"
                @delegate = Jekyll::HighlightFileBlock.new(
                    "highlight_file",
                    "#{LANG} #{SCHEMA_PATH}/#{text.strip!}.json",
                    token)
            when "include_example"
                @delegate = Jekyll::HighlightFileBlock.new(
                    "highlight_file",
                    "#{LANG} #{EXAMPLES_PATH}/#{text.strip!}.json",
                    token)
            else
                raise "Unrecognized tag name '#{tag_name}'"
            end
        end
        def render(context)
            @delegate.render context
        end
    end
end

Liquid::Template.register_tag('include_schema', Jekyll::IncludeSchema)
Liquid::Template.register_tag('include_example', Jekyll::IncludeSchema)
